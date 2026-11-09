(ns interaction.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [interaction.core :as i]))

(def state {:vars {:x 10 :y 20}})

(deftest post-increment-test
  (testing "it increments a variable and returns the previous value"
    (is (= [{:vars {:x 11 :y 20}} 10] (i/post-increment state :x)))
    (is (= [{:vars {:x 10 :y 21}} 20] (i/post-increment state :y)))
    (is (= [{:vars {:x 10 :y 20 :z 1}} 0] (i/post-increment state :z)))))

(deftest lookup-var-test
  (testing "it returns the value for a known variable and 0 otherwise"
    (is (= [state 10] (i/lookup-var state :x)))
    (is (= [state 20] (i/lookup-var state :y)))
    (is (= [state 0] (i/lookup-var state :z)))))

(deftest set-plus-test
  (testing "it evaluates numbers and variables and returns the new value"
    (is (= [{:vars {:x 10 :y 20 :z 60}} 60] (i/set-plus state :z 20 40)))
    (is (= [{:vars {:x 40 :y 20}} 40] (i/set-plus state :x :y :y)))
    (is (= [{:vars {:x 10 :y 20}} 20] (i/set-plus state :y :x 10)))
    (is (= [{:vars {:x 10 :y 20 :w 10}} 10] (i/set-plus state :w :x :nope)))))

(deftest set-var-test
  (testing "it evaluates numbers and variables and returns the new value"
    (is (= [{:vars {:x 10 :y 20 :z 20}} 20] (i/set-var state :z 20)))
    (is (= [{:vars {:x 10 :y 20 :z 10}} 10] (i/set-var state :z :x)))
    (is (= [{:vars {:x 20 :y 20}} 20] (i/set-var state :x :y)))))

(deftest there-is-a-test
  (testing "it remembers the given object"
    (is (= [{:vars {:x 10 :y 20} :objects {:shoe []}} "There is a shoe."]
           (i/there-is-a state :shoe)))
    (is (= [{:vars {:x 10 :y 20} :objects {:key []}} "There is a key."]
           (i/there-is-a state :key))))
  (testing "it notices when it already knows"
    (let [s {:objects {:shoe [:blue]}}]
      (is (= [s "I kinda already knew that."] (i/there-is-a s :shoe))))))

(deftest the-obj-is-test
  (testing "it remembers an adjective that applies to an object"
    (is (= [{:vars {:x 10 :y 20} :objects {:shoe [:blue]}} "The shoe is blue."]
           (i/the-obj-is (first (i/there-is-a state :shoe)) :shoe :blue)))
    (is (= [{:vars {:x 10 :y 20} :objects {:key [:big]}} "The key is big."]
           (i/the-obj-is (first (i/there-is-a state :key)) :key :big))))
  (testing "it refuses unknown objects"
    (is (= [state "There is no shoe."] (i/the-obj-is state :shoe :blue)))))

(deftest describe-obj-test
  (testing "it describes all adjectives for the given object, in order"
    (is (= [{:vars {:x 10} :objects {:shoe [:blue]}} "The shoe is blue."]
           (i/describe-obj {:vars {:x 10} :objects {:shoe [:blue]}} :shoe)))
    (is (= [{:vars {:x 10} :objects {:shoe [:blue :big :cute]}}
            "The shoe is blue.\nThe shoe is big.\nThe shoe is cute."]
           (i/describe-obj {:vars {:x 10} :objects {:shoe [:blue :big :cute]}} :shoe))))
  (testing "it reports unknown objects"
    (is (= [{:vars {:x 10} :objects {:key [:blue]}} "There is no shoe."]
           (i/describe-obj {:vars {:x 10} :objects {:key [:blue]}} :shoe)))))

(deftest forget-obj-test
  (testing "it removes the object"
    (is (= [{:vars {:x 10} :objects {}} "What shoe?"]
           (i/forget-obj {:vars {:x 10} :objects {:shoe [:blue]}} :shoe)))
    (is (= [{:vars {:x 10} :objects {:shoe [:blue]}} "What show?"]
           (i/forget-obj {:vars {:x 10} :objects {:shoe [:blue] :show [:big :nice]}} :show)))))

(deftest canonicalize-test
  (testing "it splits, lowercases, and converts to keywords"
    (is (= [:there :is :a :spoon] (i/canonicalize "There is a spoon")))
    (is (= [:the :spoon :is :blue] (i/canonicalize "The spoon is blue."))))
  (testing "it ignores extra whitespace"
    (is (= [:describe :the :spoon] (i/canonicalize "  describe   the spoon  "))))
  (testing "it turns digit strings into numbers"
    (is (= [:set :x :to :y :plus 10] (i/canonicalize "set x to y plus 10")))))

(deftest react-test
  (testing "variables"
    (is (= [{:vars {:x 11}} 10] (i/react {:vars {:x 10}} [:postinc :x])))
    (is (= [state 10] (i/react state [:lookup :x])))
    (is (= [{:vars {:x 40 :y 20}} 40] (i/react state [:set :x :to :y :plus :y])))
    (is (= [{:vars {:x 30 :y 20}} 30] (i/react state [:set :x :to :y :plus 10])))
    (is (= [{:vars {:x 10 :y 10}} 10] (i/react state [:set :y :to 10]))))
  (testing "objects"
    (is (= [{:vars {:x 10} :objects {:spoon []}} "There is a spoon."]
           (i/react {:vars {:x 10}} [:there :is :a :spoon])))
    (is (= [{:objects {:spoon [:plastic]}} "The spoon is plastic."]
           (i/react {:objects {:spoon []}} [:the :spoon :is :plastic])))
    (is (= [{:objects {:spoon [:plastic :blue]}} "The spoon is plastic.\nThe spoon is blue."]
           (i/react {:objects {:spoon [:plastic :blue]}} [:describe :spoon])))
    (is (= [{:objects {:spoon [:plastic :blue]}} "The spoon is plastic.\nThe spoon is blue."]
           (i/react {:objects {:spoon [:plastic :blue]}} [:describe :the :spoon])))
    (is (= [{:objects {}} "What spoon?"]
           (i/react {:objects {:spoon [:plastic :blue]}} [:forget :spoon])))
    (is (= [{:objects {}} "What spoon?"]
           (i/react {:objects {:spoon [:plastic :blue]}} [:forget :the :spoon]))))
  (testing "unknown input"
    (is (= [state "I don't know what you mean."] (i/react state [:sing :a :song])))))
