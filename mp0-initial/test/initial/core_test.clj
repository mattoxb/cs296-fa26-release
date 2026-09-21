(ns initial.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [initial.core :refer [plus socialist-plus capitalist-plus
                                  communist-plus political-extreemist-plus]]))

(deftest plus-test
  (testing "it adds numbers."
    (is (= 0 (plus)))
    (is (= 10 (plus 10)))
    (is (= 30 (plus 10 20)))
    (is (= 150 (plus 10 20 30 40 50)))))

(deftest socialist-plus-test
  (testing "it subsidizes fewer than two elements."
    (is (= 1 (socialist-plus)))
    (is (= 6 (socialist-plus 5))))
  (testing "it does nothing with two elements. Except add."
    (is (= 30 (socialist-plus 10 20))))
  (testing "it taxes the result if there are more than 2 elements."
    (is (= 59 (socialist-plus 10 20 30)))
    (is (= 98 (socialist-plus 10 20 30 40)))
    (is (= 147 (socialist-plus 10 20 30 40 50)))))

(deftest capitalist-plus-test
  (testing "it taxes fewer than two elements."
    (is (= -1 (capitalist-plus)))
    (is (= 4 (capitalist-plus 5))))
  (testing "it does nothing with two elements. Except add."
    (is (= 30 (capitalist-plus 10 20))))
  (testing "it subsidizes the result if there are more than 2 elements."
    (is (= 61 (capitalist-plus 10 20 30)))
    (is (= 102 (capitalist-plus 10 20 30 40)))
    (is (= 153 (capitalist-plus 10 20 30 40 50)))))

(deftest communist-plus-test
  (testing "it only returns 10."
    (is (= 10 (communist-plus)))
    (is (= 10 (communist-plus 5)))
    (is (= 10 (communist-plus 10 20)))
    (is (= 10 (communist-plus 10 20 30)))
    (is (= 10 (communist-plus 10 20 30 40)))
    (is (= 10 (communist-plus 10 20 30 40 50)))))

(deftest political-extreemist-plus-test
  (testing "it multiplies instead of adds."
    (is (= 1 (political-extreemist-plus)))
    (is (= 5 (political-extreemist-plus 5)))
    (is (= 200 (political-extreemist-plus 10 20)))
    (is (= 6000 (political-extreemist-plus 10 20 30)))
    (is (= 240000 (political-extreemist-plus 10 20 30 40)))
    (is (= 12000000 (political-extreemist-plus 10 20 30 40 50)))))
