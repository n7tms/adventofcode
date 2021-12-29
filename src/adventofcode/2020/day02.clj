;; Advent of Code 2020
;; Day 2
;;
;; https://adventofcode.com/2020/day/2
;;
;; Given a file similar to the following:
;;   1-3 a: abcde
;;   1-3 b: cdefg
;;   2-9 c: ccccccccc
;; verify that the passwords are valid...
;; i.e the first contains 1 to 3 a's, the second
;; contains 1 to 3 b's, and the third contains
;; 2 to 9 c's.
;;
;; Return the number of valid passwords. 

(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; This is the input data we'll be using.
(def input-file "day02-input.txt")

;; This slurp line is just for testing. (Can I read the input file?)
(slurp input-file)


(def sample "15-16 v: vvvvvvvvnvvvvcvvvvgv")
(def sample2 "1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc")


(defn parse-line [line]
  (let [[_             min   max   ch    pwd]
        (re-matches #"(\d+)-(\d+) (\w): (\w+)" line)]
    {:min (Integer/parseInt min)        ; make min a number
     :max (Integer/parseInt max)        ; make max a number
     :ch (nth ch 0)                     ; make ch a character
     :pwd pwd
     }
    ))

;; I implemented this like Fred Overflow with a reader.
;; However, I'd like to try to get this working with slurp...
;; ...just for education's sake!

(defn read-database [filename]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         (mapv parse-line)              ; "(mapv parse-line)" is the same as "(map parse-line) vec".
         ))
  )

;; check for a valid password according to part 1 rules.
(defn valid-password? [{:keys [min max ch pwd]}]
  (let [times (->> pwd
                   (filter #(= ch %))  ;; filter out of the password just the characters we care about
                   count)]             ;; count the characters we found
    (<= min times max))                ;; return true if the count is min <= count <= max
)

;; check for a valid password according to part 2 rules.
(defn valid-password2? [{:keys [min max ch pwd]}]
  (not=                                ;; 1 or the other is true, but both;
   (= ch (nth pwd (dec min)))          ;; one or the other must be true in order to return true.
   (= ch (nth pwd (dec max))))
)


(defn part1 []
  (->> input-file
       read-database
       (filter valid-password?)
       count))                        ;; returns the count of valid passwords

(defn part2 []
  (->> input-file
       read-database
       (filter valid-password2?)
       count))                        ;; returns the count of valid passwords ... according to part 2 rules

(part1) ;; => 447
(part2) ;; => 249
