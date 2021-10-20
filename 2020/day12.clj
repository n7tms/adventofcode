(ns adventofcode
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.walk :as walk]))

;; This is the input data we'll be using.
(def input-file "day12-input.txt")       ;; home
;(def input-file "./src/working/day12-input.txt")        ;; office


(def large (slurp input-file))

(def small "F10
N3
F7
R90
F11")

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (string/split s regex))

:input "F10\nN3\nF7\nR90\nF11\n"
:output [[:F 10] [:N 3] [:F 7] [:R 90] [:F 11]]
(defn parse-instruction [serialized]
  (let [[         _ action value]
        (re-matches #"(\w)(\d+)" serialized)]
    [(keyword action) (Integer/parseInt value)]))

:input {:x 17, :7 -8}
:output 25
(defn manhatten-distance [{:keys [x  y]}]
  (+
   (Math/abs x)
   (Math/abs y)))

(defn sine [a]
  (-> a
      (quot 90)
      (bit-and 3)
      [0 1 0 -1]))

(defn cosine [a]
  (-> a
      (quot 90)
      (bit-and 3)
      [1 0 -1 0]))


(defn execute1 [program]
  (manhatten-distance
   (reduce
    (fn [state [action value]]
      (case action
        :N (update state :y + value)
        :S (update state :y - value)
        :E (update state :x + value)
        :W (update state :x - value)
        :L (update state :a + value)
        :R (update state :a - value)
        :F (-> state
               (update :x + (-> state :a cosine (* value)))
               (update :y + (-> state :a   sine (* value))))))
    {:x 0
     :y 0
     :a 0}   ;; 0=east, 1=north, 2=west, 3=south
    program)))

(defn part1 []
  (->> large
       string/split-lines
       (mapv parse-instruction)
       execute1))

(part1)   ;; => 1148

(defn rotate-left [{:keys [x y] :as point} a]
  (case (quot a 90)
    0 point
    1 {:x (- y), :y    x }
    2 {:x (- x), :y (- y)}
    3 {:x    y , :y (- x)}
    ))

(defn rotate-right [{:keys [x y] :as point} a]
  (case (quot a 90)
    0 point
    1 {:x    y,  :y (- x)}
    2 {:x (- x), :y (- y)}
    3 {:x (- y), :y    x }
    ))

(defn move-forward [{sx :x, sy :y} {wx :x, wy :y} times]
  {:x (+ sx (* times wx))
   :y (+ sy (* times wy))})


(defn execute2 [program]
  (manhatten-distance
   (:ship
    (reduce
     (fn [state [action value]]
       (case action
         :N (update-in state [:waypoint :y] + value)
         :S (update-in state [:waypoint :y] - value)
         :E (update-in state [:waypoint :x] + value)
         :W (update-in state [:waypoint :x] - value)
         :L (update    state  :waypoint rotate-left value)
         :R (update    state  :waypoint rotate-right value)
         :F (update    state  :ship     move-forward (:waypoint state) value)))
     {:ship     {:x  0, :y 0}
      :waypoint {:x 10, :y 1}}
     program))))




(->> small
     string/split-lines
     (mapv parse-instruction)
     execute2
     )

(def sample '({:dir :F, :mag 10} {:dir :N, :mag 3} {:dir :F, :mag 7} {:dir :R, :mag 90} {:dir :F, :mag 11}))

