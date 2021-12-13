(ns aoc2021
  (:require [clojure.string :as string]))


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
9,0

fold along y=7
fold along x=5")

(def large (slurp input-file))

;(def active small)
(def active large)


;; ============================================================
;;              PARSE INPUT
;; ============================================================
(defn parse-c [c]
  (let [[_               x     y]
        (re-matches  #"(\d+),(\d+)" c)]
    [(Integer/parseInt x) (Integer/parseInt y)]))

(defn parse-f [f]
  (let [[_                        dir   mag]
        (re-matches #"fold along (x|y)=(\d+)" f)]
    [dir (Integer/parseInt mag)]))


(defn parse-coords [coords]
  (->> (string/split-lines coords)
       (map #(parse-c %))
       ))

(defn parse-folds [folds]
  (->> (string/split-lines folds)
       (map #(parse-f %))))

(def input
  (let [[coords folds] (string/split active #"\n *\n")]
    {:coords (parse-coords coords)
     :folds (parse-folds folds)}
    ))


;; ============================================================
;;              FOLD FUNCTIONS
;; ============================================================
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


(defn execute-folds [coords folds]
  (loop [f folds
         c coords]
    (if (empty? f) c
        (let [[dir mag] (first f)]
          (cond
              (= dir "x") (recur (rest f) (fold-x c mag))
              (= dir "y") (recur (rest f) (fold-y c mag)))))))


;; ============================================================
;;              SOLUTIONS
;; ============================================================

(defn part1 []
"Count the number of visible dots after one fold"
  (count
   (distinct
    (execute-folds (:coords input) (list (first (:folds input))))))
  
  )

(defn part2 []
"after the folds, what eight letters appear on the paper"
  (use '(incanter core stats charts))
  (let [x (for [[x y] (execute-folds (:coords input) (:folds input))] x)
        y (for [[x y] (execute-folds (:coords input) (:folds input))] y)]
    (view (scatter-plot x y))
    )
  )

(println "Part1: " (part1) "\nPart2: " (part2))


