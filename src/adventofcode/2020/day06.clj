;; Advent of Code 2020
;; Day 6
;;
;; https://adventofcode.com/2020/day/6

(ns user
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

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

(def input-file "day06-input.txt")

(defn count-any-yes [serialized]
  (-> serialized
      set
      (disj \newline)
      count))

;; testing the count-any-yes function
(count-any-yes "abcx\nabcy\nabcz")  ;; => 6


(defn count-all-yes [serialized]
  (->> serialized
       (split #"\n")
       (map set)
       (apply set/intersection)
       count
       ))

;; testing the count-all-yes function
(count-all-yes "abcx\nabcy\nabcz")  ;; => 3


;; Part 1 - Count any yes
(defn part1 []
  (->> input-file
       slurp
       (split #"\n\n")
       (map count-any-yes)
       (reduce +)))   ;672

;; Part 2 - Count all yes
(defn part2 []
  (->> input-file
       slurp
       (split #"\n\n")
       (map count-all-yes)
       (reduce +)))   ;3268

(part1)    ;; => 6612
(part2)    ;; => 3268


;; ========================================================
;; Test code

(split #"\n\n" sample)     ; ["abc" "a\nb\nc" "ab\nac" "a\na\na\na" "b"]

(-> sample
    set
    (disj \newline)
    count)

