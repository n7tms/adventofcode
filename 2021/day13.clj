(ns aoc2021
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "13")
(def input-file (str "day" day "-input.txt"))

(def small "6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0")
(def small-fold "fold along y=7
fold along x=5")

(def large (slurp input-file))
(def large-fold "fold along x=655
fold along y=447
fold along x=327
fold along y=223
fold along x=163
fold along y=111
fold along x=81
fold along y=55
fold along x=40
fold along y=27
fold along y=13
fold along y=6")

(def instr '(["x" 655] ["y" 447] ["x" 327] ["y" 223] ["x" 163] ["y" 111] ["x" 81] ["y" 55] ["x" 40] ["y" 27] ["y" 13] ["y" 6]))

;(def instr '(["y" 7] ["x" 5]))


;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))


(defn parse-line [line]
  (let [[_              x    y]
        (re-matches  #"(\d+),(\d+)" line)]
    [(Integer/parseInt x) (Integer/parseInt y)]))


(def coords
  (->> (string/split-lines active)
       (map #(parse-line %))
       ))



(defn max-y [matrix]
  (apply max (for [[x y] matrix] y)))

(defn max-x [matrix]
  (apply max (for [[x y] matrix] x)))


(defn fold-y [coords y-line]
  (for [points coords]
    (let [[x y] points]
      (if (> y y-line)
        [x (- y-line (- y y-line))]
        [x y]))))

(defn fold-x [coords x-line]
  (for [points coords]
    (let [[x y] points]
      (if (> x x-line)
        [(- x-line (- x x-line)) y]
        [x y]))))


(def plot-points 
  (loop [i instr
         c coords]
    (if (empty? i) c
        (let [[dir mag] (first i)]
          (cond
              (= dir "x") (recur (rest i) (fold-x c mag))
              (= dir "y") (recur (rest i) (fold-y c mag)))))))




(spit "day13-output.txt" (string/join " " (for [[x y] plot-points]
                                            (str "(" x "," y ")"))))



 ;; between 808 and 922




(defn part1 []

  )




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (clojure.test/is (= (part1) 17))
  (clojure.test/is (= (part2) 00)))

(run-tests)

