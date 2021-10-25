1048576;; Advent of Code
;; Day 18


(ns adventofcode
  (:require [clojure.string :as str]))

(defn split [regex s]
  (str/split s regex))


(def small1 "1 + 2 * 3 + 4 * 5 + 6")                            ;; => 71
(def small2 "1 + (2 * 3) + (4 * (5 + 6))")                      ;; => 51
(def small3 "2 * 3 + (4 * 5)")                                  ;; => 26
(def small4 "5 + (8 * 3 + 9 + 3 * 4 * 3)")                      ;; => 437
(def small6 "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")        ;; => 12240
(def small5 "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")  ;; => 13632

(def large (slurp "day18-input.txt"))



;; I thought this was going to be a straight forward implementation of a "compiler",
;; but I ended up getting bogged down in the clojure constructs again. So, again,
;; I turned to Fred ... and his solution was WAY over my head. I'm not sure I would
;; have ever gotten here using clojure!!!

;; expression = element { ( *|+ ) } element
;; element    = number | ( expression )

(defn evaluate [^String input]
  (let [lexemes (volatile! (re-seq #"\d|[+*()]" input))]
    (letfn [(current [] (first (deref lexemes)))

            (accept [] (vswap! lexemes rest))

            (expression []
              (loop [temp (primary)]
                (case (current)
                  "*" (do
                        (accept)
                        (recur (* temp (primary))))
                  "+" (do
                        (accept)
                        (recur (+ temp (primary))))
                  temp)))

            (primary []
              (case (current)
                "(" (do
                      (accept)
                      (let [temp (expression)]
                        (accept) ;; assume closing paren
                        temp))
                (let [temp (Integer/parseInt (current))]
                  (accept)
                  temp)))]
      (expression))))


(defn part1 [input]
  (->> input
       string/split-lines
       (map evaluate)
       (reduce +)))

(part1 large)   ;; => 4491283311856

;; Part 2 ==========================================

;; product  = sum { '*' sum }
;; sum      = primary { '+' primary }
;; primary  = number | '(' product ')


(defn evaluate2 [^String input]
  (let [lexemes (volatile! (re-seq #"\d|[+*()]" input))]
    (letfn [(current [] (first (deref lexemes)))

            (accept [] (vswap! lexemes rest))

            (product []
              (loop [temp (sum)]
                (case (current)
                  "*" (do
                        (accept)
                        (recur (* temp (sum))))
                  temp)))

            (sum []
              (loop [temp (primary)]
                (case (current)
                  "+" (do
                        (accept)
                        (recur (+ temp (primary))))
                  temp)))

            (primary []
              (case (current)
                "(" (do
                      (accept)
                      (let [temp (product)]
                        (accept) ;; assume closing paren
                        temp))
                (let [temp (Integer/parseInt (current))]
                  (accept)
                  temp)))]
      (product))))

(defn part2 [input]
       (->> input
            string/split-lines
            (map evaluate2)
            (reduce +)))

(part2 large)  ;; => 68852578641904
