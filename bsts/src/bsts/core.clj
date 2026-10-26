(ns bsts.core)

(defrecord BST [data left right])

(defn bst "Create a BST"
  ([elt]       (->BST elt nil nil))
  ([elt lc rc] (->BST elt lc rc)))
