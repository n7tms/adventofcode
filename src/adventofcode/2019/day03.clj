(ns adventofcode
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "03")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "R8,U5,L5,D3\nU7,R6,D4,L4")
(def small2 "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
(def small3 "R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83")

(def large (slurp input-file))

;(def active small)
(def active large)
;(def active small3)

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
                    (list
                     (distinct (partition 2 2 (flatten (build-path (first lines)))))
                     (distinct (partition 2 2 (flatten (build-path (last lines))))))))]
    (filter (fn [[k v]] (> v 1)) (frequencies x))))




(defn man-dist [[x y]]
  (+ (Math/abs x) (Math/abs y)))








(defn part1 []
  (first (sort (map #(man-dist %) (keys intersections))))
  )




(defn part2 []

  )


(println "AoC 2019 - Day 03")
(println "Part1: " (part1) "\nPart2: " (part2))





