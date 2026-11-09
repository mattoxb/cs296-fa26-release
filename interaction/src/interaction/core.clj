(ns interaction.core
  (:require [clojure.string :as str]
            [clojure.core.match :refer [match]]))

;; # Interaction
;; A simple parser that can remember facts.
;;

;; # Actions
;;
;; Let an *action* be a function with a specific
;; interface: It will take in a *state* followed by
;; a sequence of arguments.  It will return a vector
;; consisting of a new state and a response.
;;
;; Here is a simple example, a post-increment function.
;; Assume the state is a hash-map with a key :vars
;; and a value containing a hash of the variables and
;; their values.

(defn post-increment
  "Action: post-increment
   Returns the value of a variable from the state and increments it.
   An undefined variable counts as 0."
  [state var]
  (if-let [val (get-in state [:vars var])]
    [(update-in state [:vars var] inc) val]
    [(assoc-in state [:vars var] 1) 0]))

;; ```
;; interaction.core=> (post-increment {:vars {:x 10}} :x)
;; [{:vars {:x 11}} 10]
;; ```

;; ## Your work
;;
;; Fill in the code for the functions below that have empty bodies.
;; The others are done for you; read them first.

(defn lookup-var
  "Action: lookup-var.  Return the value of var if it has been defined,
  otherwise 0.  The state is unchanged."
  [state var]
  (if-let [val (get-in state [:vars var])]
    [state val]
    [state 0]))

;; ```
;; interaction.core=> (lookup-var {:vars {:x 10}} :x)
;; [{:vars {:x 10}} 10]
;; ```

(defn value-of
  "Helper (not an action).  An argument may be a number or a variable name.
  Return the number itself, or the variable's current value."
  [state e]
  (if (number? e)
    e
    (second (lookup-var state e))))

(defn set-plus
  "Action: set-plus.  Set var = e1 + e2, return the sum as a result."
  [state var e1 e2]
  (let [s (+ (value-of state e1) (value-of state e2))]
    [(assoc-in state [:vars var] s) s]))

;; ```
;; interaction.core=> (set-plus {:vars {:x 10}} :y :x 20)
;; [{:vars {:x 10 :y 30}} 30]
;; ```

(defn set-var
  "Action: set-var. Set var = e1.  Return the new value as a result."
  [state var e1]
  )

;; ```
;; interaction.core=> (set-var {:vars {:x 10}} :y :x)
;; [{:vars {:x 10 :y 10}} 10]
;; ```

;; Objects are stored under the :objects key.  Each object maps to a *vector*
;; of adjectives, in the order they were learned.

(defn there-is-a
  "Action: there-is-a.  Remember that an object obj exists.
  Returns \"There is a obj.\" as a result."
  [state object]
  (if (get-in state [:objects object])
    [state "I kinda already knew that."]
    [(assoc-in state [:objects object] []) (str "There is a " (name object) ".")]))

;; ```
;; interaction.core=> (there-is-a {:vars {:x 10}} :shoe)
;; [{:vars {:x 10} :objects {:shoe []}} "There is a shoe."]
;; ```

(defn the-obj-is
  "Action: the-obj-is.  Remember an adjective that applies to an object.
  Returns \"The obj is adj.\" as a result."
  [state object adj]
  (if (get-in state [:objects object])
    [(update-in state [:objects object] conj adj)
     (str "The " (name object) " is " (name adj) ".")]
    [state (str "There is no " (name object) ".")]))

;; ```
;; interaction.core=> (the-obj-is {:vars {:x 10} :objects {:shoe []}} :shoe :blue)
;; [{:vars {:x 10} :objects {:shoe [:blue]}} "The shoe is blue."]
;; ```

(defn describe-obj
  "Action: describe-obj.  If the object exists, the result is one line
  \"The obj is adj.\" per adjective, joined with newlines.  If not, the
  result is \"There is no obj.\""
  [state object]
  )

;; ```
;; interaction.core=> (describe-obj {:vars {:x 10} :objects {:shoe [:blue :big]}} :shoe)
;; [{:vars {:x 10}, :objects {:shoe [:blue :big]}} "The shoe is blue.\nThe shoe is big."]
;; ```

(defn forget-obj
  "Action: forget-obj.  Delete the given object and return \"What obj?\""
  [state object]
  )

;; ```
;; interaction.core=> (forget-obj {:objects {:show [:exciting]}} :show)
;; [{:objects {}} "What show?"]
;; ```

;; # Parsing
;;
;; This is mostly code from the lecture.

(defn canonicalize
  "Given an input string, split it into words (dropping whitespace and
  punctuation), lowercase them, and return a vector.  Words made only of
  digits become numbers; everything else becomes a keyword."
  [input]
  )

;; ```
;; interaction.core=> (canonicalize "The shoe is blue.")
;; [:the :shoe :is :blue]
;; interaction.core=> (canonicalize "set x to y plus 10")
;; [:set :x :to :y :plus 10]
;; ```

;; # Dispatch
;;
;; `react` uses `core.match` to pick an action based on the shape of the
;; input.  A keyword in a pattern must match exactly; a bare symbol (like
;; `obj` below) matches anything and is bound to what it matched.
;;
;; Phrases to support:
;;
;; ```
;; postinc x                 lookup x
;; set x to e                set x to e1 plus e2
;; there is a obj            the obj is adj
;; describe obj              describe the obj
;; forget obj                forget the obj
;; ```

(defn react
  "Given a state and a canonicalized input vector, call the matching action.
  If there is no match, return the original state and the result
  \"I don't know what you mean.\""
  [state input-vector]
  (match input-vector
    [:postinc v]        (post-increment state v)
    [:there :is :a obj] (there-is-a state obj)
    ;; add the other phrases here
    :else [state "I don't know what you mean."]))

;; ```
;; interaction.core=> (react {:vars {:x 10}} [:postinc :x])
;; [{:vars {:x 11}} 10]
;; ```

(defn repl
  "Run an interactive loop starting from the given state.  Prints a prompt,
  reads a line, reacts to it, prints the result, and loops with the new state.
  Quits when you say bye (or at end of input).  Returns the final state."
  [initial-state]
  (println "Welcome!  Let's talk.")
  (loop [state initial-state]
    (print "> ")
    (flush)
    (let [line (read-line)]
      (if (or (nil? line) (= [:bye] (canonicalize line)))
        state
        (let [[new-state result] (react state (canonicalize line))]
          (println result)
          (recur new-state))))))

;; ```
;; interaction.core=> (repl {})
;; Welcome!  Let's talk.
;; > there is a spoon.
;; There is a spoon.
;; > the spoon is blue.
;; The spoon is blue.
;; > the spoon is big.
;; The spoon is big.
;; > describe the spoon.
;; The spoon is blue.
;; The spoon is big.
;; > forget the spoon.
;; What spoon?
;; > describe the spoon.
;; There is no spoon.
;; > bye
;; {:objects {}}
;; interaction.core=>
;; ```

(defn -main
  "Start the REPL with an empty state.  Run with `clojure -M:run`."
  [& args]
  (repl {}))
