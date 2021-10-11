;; Advent of Code 2020
;; Day 11
;;
;; https://adventofcode.com/2020/day/11
;;
;; Description of problem
;;
;;
;;
;;
;;

(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; This is the input data we'll be using.
(def input-file "day11-input.txt")

;; This slurp line is just for testing. (Can I read the input file?)
(slurp input-file)

(def seating
  (slurp input-file))

(def sample "L.#L.L#.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL")



(defn read-lines [filename]
  (str/split-lines (slurp filename)))

;(read-lines sample)
sample
(count (first (str/split-lines seating)))

(defn occupied-adjacent []
)

(defn occupied? [r c]
  (nth sample 0)
  )

(let [row 0
      col 0]
  
  )

(clojure.string/replace (first (str/split-lines sample)) #"L|#" {"L" "0" "#" "1"})



