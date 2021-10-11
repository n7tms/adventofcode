(ns adventofcode
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.walk :as walk]))

;; This is the input data we'll be using.
;(def input-file "day12-input.txt")       ;; home
(def input-file "./src/working/day12-input.txt")        ;; office


;; This slurp line is just for testing. (Can I read the input file?)
;(slurp input-file)

(def large (slurp input-file))

(def small "F10
N3
F7
R90
F11")


(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (string/split s regex))

(defn parse-passport [serialized]
  (->> serialized              ; "a:1 b:2 c:3"
       (split #"\s")           ; ["a:1" "b:2" "c:3"]
       (map #(split #":" %))   ; (["a" "1"] ["b" "2"] ["c" "3"])
       (into {})               ; {"a" "1", "b" "2", "c" "3"}
       walk/keywordize-keys    ; {:a "1", :b "2", :c "3"}
        ))

(defn turn [facing dir mag]
  
  )

(defn move [nav]
  (let [[_             dir   mag]
        (re-matches #"([NSWEFRL])(\d+)" nav)]
    {:dir (keyword dir)
     :mag (Integer/parseInt mag)        ; make min a number
     }
    )
  )

(defn navigate [instr]
  (loop [idx 0 facing :E North 0 East 0]
    (while (< idx (count instr))
      (cond
        (= :N (nth instr idx)) (recur (inc idx) facing (+ North (:mag (nth instr idx))) East)
        (= :S (nth instr idx)) (recur (inc idx) facing (- North (:mag (nth instr idx))) East)
        (= :E (nth instr idx)) (recur (inc idx) facing North (+ East  (:mag (nth instr idx))))
        (= :W (nth instr idx)) (recur (inc idx) facing North (- East  (:mag (nth instr idx))))
        )
     
)
  {North East}    )
 
)

;; The following is Fred's code. Adopt it to work with my logic
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






(->> small
     (split #"\s")
     (map #(move %))  ;; ({:dir :F, :mag 10} {:dir :N, :mag 3} {:dir :F, :mag 7} {:dir :R, :mag 90} {:dir :F, :mag 11})
     count
;     navigate
     )

(def sample '({:dir :F, :mag 10} {:dir :N, :mag 3} {:dir :F, :mag 7} {:dir :R, :mag 90} {:dir :F, :mag 11}))
(:dir (nth sample 0 ))
(navigate sample)






                                        ;(filter #("([L|R])(\d+)"))

