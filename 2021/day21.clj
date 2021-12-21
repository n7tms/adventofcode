(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))

;; TODO: change 00 to current day number
;(def day "21")
;(def input-file (str "day" day "-input.txt"))

(def small [4 8])
(def large [8 1])

(def active small)
;(def active large)



(defn part1 []
  (loop [dice 0
         rolls 0
         pos1 (first active)
         pos2 (second active)
         score1 0
         score2 0
         turn 1]
    
    (if (>= score1 1000)     (* score2 rolls)
        (if (>= score2 1000) (* score1 rolls)
            (if (= turn 1)
              ;; player 1's turn
              (let [d (+ (mod (+ 1 dice) 100) (mod (+ 2 dice) 100) (mod (+ 3 dice) 100))
                    p1 (if (zero? (mod (+ d pos1) 10)) 10 (mod (+ d pos1) 10))
                    s1 (+ p1 score1)]
                (recur (+ dice 3) (+ rolls 3) p1 pos2 s1 score2 2))

              ;; player 2's turn
              (let [d (+ (mod (+ 1 dice) 100) (mod (+ 2 dice) 100) (mod (+ 3 dice) 100))
                    p2 (if (zero? (mod (+ d pos2) 10)) 10 (mod (+ d pos2) 10))
                    s2 (+ p2 score2)]
                (recur (+ dice 3) (+ rolls 3) pos1 p2 score1 s2 1)))))))



(defn part2 []

  )

;(part1)   ;; => 518418


(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= (part1) 739785))
  (test/is (= (part2) 0)))

(run-tests)

