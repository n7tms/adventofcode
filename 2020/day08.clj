;; Advent of Code 2020
;; Day 08
;;
;; https://adventofcode.com/2020/day/8
;;
;; Description of problem
;; Navigate instruction code
;;
;;
;;
;;

(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; This is the input data we'll be using.
(def input-file "day08-input.txt")

;; This slurp line is just for testing. (Can I read the input file?)
(slurp input-file)

(def sample 
"nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6")

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (str/split s regex))

(defn read-lines [filename]
  (str/split-lines (slurp filename)))

(def aline "acc -99")

(let [[_        op       dir   dist]
      (re-matches #"(\w{3}) ([\+|\-])(\d+)" aline)]
  {:op op
   :dir dir
   :dist dist}
  )




