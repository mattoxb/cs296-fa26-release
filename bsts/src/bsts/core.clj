(ns bsts.core)

(defrecord BST [data left right])

(defn bst "Create a BST"
  ([elt]       (->BST elt nil nil))
  ([elt lc rc] (->BST elt lc rc)))

(defn add-triple [v]
  (let [a (v 0)
        b (v 1)
        c (v 2)]
    (+ a b c)))

(defn add-triple [v]
  (+ (v 0) (v 1) (v 2)))

(defn add-triple [v]
  (let [[x y z] v]
    (+ x y z)))

(defn get-data [t]
  (let [{:keys [data left right]} t]
    data))

(defn mirror [t]
  (if (nil? t) nil
      (let [{:keys [data left right]} t]
        (bst data (mirror right) (mirror left)))))

(defn find' [t desire]
  "Your code here!!"
  (do
  (print t)
  (case (nil? t)             false
        (= (:data t) desire) true
        (< desire (:data t)) (find' (:left t) desire)
        (find' (:right t) desire)))
)

(defn find' [t desire]
  "Your code here!!"
  (if (nil? t) false
      (or (= (:data t) desire) 
          (and (< desire (:data t)) (find' (:left t) desire))
          (find' (:right t) desire))))

(defn add [t nu-elt]
  (let [{:keys [data left right]} t]
    (cond (nil? t)        (bst nu-elt)
          (= data nu-elt) t
          (< nu-elt data) (bst data (add left nu-elt) right)
          :else           (bst data left (add right nu-elt)))))

(defn go-left [t]
  (if (nil? (:left t)) t
      (go-left (:left t))))

(defn ios [t]
  (go-left (:right t)))
  
(defn delete [t victim]
  (let [{:keys [data left right]} t]
    (cond (nil? t)        nil
          (= data victim) (cond (nil? left) right
                                (nil? right) left
                                :neither    (let [the-ios (ios t)
                                                  nu-right (delete right (:data the-ios))]
                                              (bst (:data the-ios) left nu-right)))
          (< victim data) (bst data (delete left victim) right)
          :else           (bst data left (delete right victim)))))

  
