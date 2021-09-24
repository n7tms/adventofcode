(ns aoc2020.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

;; Advent of Code 2020
;; Day 6

;; Sample Data
(def sample "abc

a
b
c

ab
ac

a
a
a
a

b")

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (str/split s regex))


(defn count-any-yes [serialized]
  (-> serialized
      set
      (disj \newline)
      count))

(count-any-yes "abcx\nabcy\nabcz")


(defn count-all-yes [serialized]
  (->> serialized
       (split #"\n")
       (map set)
       (apply set/intersection)
       count
       ))

;; Part 1 - Count any yes
(->> "/home/todd/dev/clojure/aoc2020/puzzlefiles/day6.txt"
     slurp
     (split #"\n\n")
     (map count-any-yes)
     (reduce +))   ;672

;; Part 2 - Count all yes
(->> "/home/todd/dev/clojure/aoc2020/puzzlefiles/day6.txt"
     slurp
     (split #"\n\n")
     (map count-all-yes)
     (reduce +))   ;3268




;; Test code
(count-all-yes "abcx\nabcy\nabcz")

(split #"\n\n" sample)     ; ["abc" "a\nb\nc" "ab\nac" "a\na\na\na" "b"]

(split #"\n\n" sample)

(-> sample
    set
    (disj \newline)
    count)

(+ 1 1)
