(ns adventure.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [adventure.core :as a]))

;; Add tests for your own actions here.  Actions that print can be tested
;; with with-out-str, or by checking only the returned state.

(deftest canonicalize-test
  (is (= [:go :north] (a/canonicalize "Go North!")))
  (is (= [:take :the :egg] (a/canonicalize "  take  the egg. "))))

(deftest go-test
  (testing "moving through an exit"
    (is (= :grue-pen
           (get-in (a/go a/init-state :south) [:adventurer :location]))))
  (testing "no exit that way"
    (is (= a/init-state
           (binding [*out* (java.io.StringWriter.)]
             (a/go a/init-state :east))))))
