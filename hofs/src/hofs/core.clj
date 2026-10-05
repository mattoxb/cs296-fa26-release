(ns hofs.core)

(defn myplus [x y & rest]
  (apply + (cons x (cons y rest))))

(defn commie-myplus [x y & rest]
  10)

(defn capitalist-plus [x y & rest]
  (let [val (apply + (cons x (cons y rest)))]
    (if (> val 10) (* val 2) (/ val 2))))

(myplus 2 3 4 5) 

(def v1 [myplus commie-myplus capitalist-plus]) 

(defn neg [x] (- x))

(defn neg-list [x]
  (if (empty? x) '()
      (cons (- (first x))
            (neg-list (rest x)))))

(defn map-1 [f x]
  (if (empty? x)
    '()
    (cons (f (first x))
          (map-1 f (rest x)))))

(defn map-m [f & x]
  (if (some empty? x)
    '()
    (cons (apply f (map first x))
          (apply map-m f (map rest x)))))

(apply + '(1 2 3)) ==>  (+ 1 2 3)

(apply map-m f '((1 2 3) (10 23 40))) => (map-m f '(1 2 3) '(10 23 40))

(map-1 neg '(1 3 4 5 6))

(map-m + '(1 3 2 3 4) '(20 30 40 50))

(apply + '(1 20))

((v1 2) 20 20)

(def neg1 #(* -1 %))
(def neg2 (fn [x] (* -1 x)))

  
(defn twice [f x]
  (f (f x)))
  
(inc 10)
(twice inc 10)

empty
