(ns adventofcode
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "03")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "R8,U5,L5,D3\nU7,R6,D4,L4")

(def large (slurp input-file))

;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))

(def lines
  (->> active
       (string/split-lines)
       (map #(split #"," %))))


(defn parse-instr [instr]
  (let [[_             dir  mag]
        (re-matches #"(\w)(\d+)" instr)]
    [dir (Integer/parseInt mag)]
    )  )


;; start pos = [0 0]
;; read first instr
;; . U3  => [1 0] [2 0] [3 0]
;; current pos = [3 0]
;; read next instr
;; . L2  => [3 -1] [3 -2]
;; current pos = [3 -2]
;; 

(defn make-points [pos offset] 
  (let [[pr pc] pos
        [or oc] offset]

    (cond
      (and (zero? or) (> oc 0)) (for [i (range (inc pc) (+ pc oc 1))]     [pr i])  ;; right
      (and (zero? or) (< oc 0)) (for [i (range (dec pc) (+ pc oc -1) -1)] [pr i])  ;; left
      (and (zero? oc) (> or 0)) (for [i (range (inc pr) (+ pr or 1))]     [i pc])  ;; down
      (and (zero? oc) (< or 0)) (for [i (range (dec pr) (+ pr or -1) -1)] [i pc])) ;; up
    )
  )


(defn build-path [line]
  (loop [instrs line
         pos [0 0]   ;; current pos
         path []]
    (if (empty? instrs)
      path
      (let [[dir mag] (parse-instr (first instrs))
            [r c] pos]
        (case dir
          "U" (recur (rest instrs) [(- r mag) c] (conj path (make-points pos [(- mag) 0])))
          "D" (recur (rest instrs) [(+ r mag) c] (conj path (make-points pos [   mag  0])))
          "L" (recur (rest instrs) [r (- c mag)] (conj path (make-points pos [0 (- mag)])))
          "R" (recur (rest instrs) [r (+ c mag)] (conj path (make-points pos [0    mag ])))
          )))))




(def intersections
  (let [x 
        (partition 2 2
                   (flatten
                    (list (build-path (first lines)) (build-path (last lines)))))]
    (filter (fn [[k v]] (> v 1)) (frequencies x))))

(sort (keys intersections))








(defn part1 []

  )




(defn part2 []

  )


(println "AoC 2019 - Day 03")
(println "Part1: " (part1) "\nPart2: " (part2))



(def my-line [[5 10] [10 20]])

(let [[[a b :as group1] [c d :as group2]] my-line]
  (println a b group1)
  (println c d group2))


