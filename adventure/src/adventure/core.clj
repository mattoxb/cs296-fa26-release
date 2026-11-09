(ns adventure.core
  (:require [clojure.string :as str]
            [clojure.core.match :refer [match]]))

;; # Adventure
;;
;; A starting point for the adventure project.  This runs as-is: you can walk
;; between the two rooms and quit.  Everything else is up to you.
;; See adventure.md for the requirements.

;; # The world

(def init-rooms
  {:foyer {:desc "The walls are freshly painted but do not have any pictures.  You get the feeling it was just created for a game or something."
           :title "in the foyer"
           :dir {:south :grue-pen}
           :contents #{:raw-egg}}
   :grue-pen {:desc "It is very dark.  You are about to be eaten by a grue."
              :title "in the grue pen"
              :dir {:north :foyer}
              :contents #{}}})

(def init-items
  {:raw-egg {:desc "This is a raw egg.  You probably want to cook it before eating it."
             :name "a raw egg"}})

(def init-adventurer
  {:location :foyer
   :inventory #{}
   :tick 0
   :seen #{}})

(def init-state
  {:rooms init-rooms
   :items init-items
   :adventurer init-adventurer})

;; # Parsing

(defn canonicalize
  "Turn an input line into a vector of keywords.
  \"Go North!\" => [:go :north]"
  [input]
  (->> (str/split (str/lower-case input) #"[^a-z0-9]+")
       (remove str/blank?)
       (mapv keyword)))

;; # Actions
;;
;; Unlike the interaction MP, these actions print their output directly and
;; return only the new state.

(defn status
  "Say where the adventurer is.  Give the long description on the first visit."
  [state]
  (let [location (get-in state [:adventurer :location])
        room (get-in state [:rooms location])]
    (print (str "You are " (:title room) "."))
    (when-not (contains? (get-in state [:adventurer :seen]) location)
      (print (str " " (:desc room))))
    (println)
    (update-in state [:adventurer :seen] conj location)))

(defn go
  "Move in direction dir, if there is an exit that way."
  [state dir]
  (let [location (get-in state [:adventurer :location])
        dest (get-in state [:rooms location :dir dir])]
    (if (nil? dest)
      (do (println "You can't go that way.")
          state)
      (assoc-in state [:adventurer :location] dest))))

(defn quit [state]
  (println "Goodbye!")
  (assoc state :done true))

;; # Dispatch

(defn respond
  "Given a state and a canonicalized command, carry it out and return the new state."
  [state command]
  (match command
    [:north]       (go state :north)
    [:go :north]   (go state :north)
    [:south]       (go state :south)
    [:go :south]   (go state :south)
    ;; TODO: n/s/e/w, east, west, look, examine, inventory, take, drop, ...
    [:quit]        (quit state)
    :else          (do (println "I don't understand.") state)))

;; # Main loop

(defn -main
  "Initialize the adventure.  Run with `clojure -M:run`."
  [& args]
  (loop [state init-state]
    (let [state (status state)
          _ (println "What do you want to do?")
          _ (print "> ")
          _ (flush)
          line (read-line)]
      (if (nil? line)                   ; end of input (Ctrl+D)
        (quit state)
        (let [new-state (respond state (canonicalize line))]
          (when-not (:done new-state)
            (recur (update-in new-state [:adventurer :tick] inc))))))))
