(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; Advent of Code
;; Day 08
;;
;; This implementation is largly from Fred Overflow
;; with some adaptations to my style and preferences.


;(def input-file "day08-input.txt")
(def input-file "src/working/day08-input.txt")

:input "acc -2"
;; Receives a line of code from the input and converts it to a 
;; vector containing a keyword (the operation) and an argument (some value)
;; input => "acc -2"
;; output => [:acc -2]
(defn parse-instr [serialized]
  (let [[      _         op         arg]
        (re-matches #"(acc|jmp|nop) ([+-]\d+)" serialized)]
    [(keyword op)
     (Integer/parseInt arg)]))

;(parse-instr "acc -2")

;; Part 1 meat of the solution
;; loops through the code (input-file), keeping track of the 
;; accumulator (accum), the current line of code (idx) and 
;; whether that particular line of code has been previously
;; visited (visited).
;; If the instruction has been visited previously, it returns
;; the accumulator value.
(defn run [program]
  (loop [accum 0
         idx 0
         visited #{}]
    (if (contains? visited idx)
      {:status :loops, :acc accum}
      (if (= idx (count program))
        {:status :halts, :acc accum}
        (let [[op arg] (program idx)]
          (case op
            :acc (recur (+ accum arg) (inc idx)   (conj visited idx))
            :jmp (recur accum         (+ idx arg) (conj visited idx))
            :nop (recur accum         (inc idx)   (conj visited idx))
            )))))
  )

;; this block is used for testing the parsing and logic.
(->> "acc +2
acc +3
acc +5
nop +3
acc +7
jmp -5"
     str/split-lines
     (mapv parse-instr)
     run)

(def the-code
  (->> (slurp input-file)
      str/split-lines
      (mapv parse-instr)))


;; Using the input-file, split the lines and then parse each
;; of them into a vector. Then throw that entire vector at
;; the part1 run, above.
(defn part1 []
  (->> the-code
       run
       :acc
       ))
(part1)  ;; => 1134


(defn part2 []
  (->> the-code
       count
       range
       (map #(update-in the-code [% 0]
               {:jmp :nop
                :nop :jmp
                :acc :acc} ))
       (map run)
       (filter #(= :halts (:status %)))
       first
       :acc
  )
)

(part2)   ;; => 1205



;; Fred is using the following pattern to change the jmps to nops and the nops to jmps
(def odds [[1 3] [5 7]])
(update-in odds [1 0] inc)
