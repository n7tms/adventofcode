(ns adventofcode
  (:require [clojure.string :as string]
            [clojure.test :as test]))


(def day "02")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "1,9,10,3,2,3,11,0,99,30,40,50\n")
(def large (slurp input-file))

;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))

(def input
  (->> active
       string/trim
       (split #",")
       (mapv #(Integer/parseInt %))))

(defn init-mem [memory noun verb]
  (vec (flatten (list (first memory) noun verb (nthrest memory 3)))))



(defn exc [op ic s1 s2 d]
  (let [v (op (nth ic s1) (nth ic s2))]
    (if (zero? d)
      (vec (flatten (list v (rest ic))))
      (vec (flatten (list (take d ic) v (nthrest ic (inc d))))))
    ))



(defn intcode [input]
  (loop [in input
         idx 0]
    (let [instr (nth in idx)
          src1 (nth in (+ idx 1))
          src2 (nth in (+ idx 2))
          dest (nth in (+ idx 3))]
      (cond
        (= instr 99) in
        (= instr  1) (recur (exc + in src1 src2 dest) (+ idx 4))
        (= instr  2) (recur (exc * in src1 src2 dest) (+ idx 4))
        )
      )
    ))


(defn part1 []
  (first (intcode (init-mem input 12 2)))   ;; => 3790645
  )


(defn part2 []
  (first
   (remove nil?
                (for [n (range 100)
                      v (range 100)]
                  (let [output (first (intcode (init-mem input n v)))]
                    (if (= output 19690720) (+ v (* 100 n)))))))
  )   ;; => 6577


(println "AoC 2019 - Day 2")
(println "Part1: " (part1) "\nPart2: " (part2))



