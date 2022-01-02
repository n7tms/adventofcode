(ns adventofcode
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "03")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "R8,U5,L5,D3\nU7,R6,D4,L4")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(def lines (string/split-lines active))

(defn part1 []

  )




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))



