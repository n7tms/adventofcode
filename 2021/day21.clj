(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))

;; TODO: change 00 to current day number
;(def day "21")
;(def input-file (str "day" day "-input.txt"))

(def small [4 8])
(def large [8 1])

;(def active small)
(def active large)



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


;; Part 2 solution from 
;; https://github.com/wevre/advent-of-code/blob/master/src/advent_of_code/2021/day_21.clj
;; (def p1 8)
;; (def p2 1)

(defn wrap-10 [x] (inc (mod (dec x) 10)))

(def quantum
  (memoize
   (fn
     ([p1 p2] (quantum p1 p2 0 0))
     ([p1 p2 sc1 sc2]
      (if (<= 21 sc2)
        [0 1]
        (reduce #(mapv + %1 %2)
                (for [r1 [1 2 3] r2 [1 2 3] r3 [1 2 3]
                      :let [p1 (wrap-10 (+ p1 r1 r2 r3))
                            sc1 (+ sc1 p1)]]
                  (reverse (quantum p2 p1 sc2 sc1)))))))))

(defn part2 []
 (apply max (quantum (first active) (last active))))


;(part1)   ;; => 518418
;(part2)   ;; => 116741133558209

(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= (part1) 739785))
  (test/is (= (part2) 444356092776315)))

(run-tests)



;; This part2 solution...
;; https://github.com/pbruyninckx/aoc2021/blob/main/src/aoc/day21_2.clj
;; ... looks a little more like my style ... lots of functions, etc.
