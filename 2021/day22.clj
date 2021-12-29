(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))


(def day "22")
(def input-file (str "day" day "-input.txt"))

(def small "on x=10..12,y=10..12,z=10..12
on x=11..13,y=11..13,z=11..13
off x=9..11,y=9..11,z=9..11
on x=10..10,y=10..10,z=10..10")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(defn parse-line [line]
  (let [[_             state  x1    x2     y1 y2  z1 z2]
        (re-matches  #"(on|off) x=(\d+)\.\.(\d+),y=(\d+)\.\.(\d+),z=(\d+)\.\.(\d+)" line)]
    [[x1 x2] [y1 y2] [z1 z2]]))

(def parsed
  (->> (string/split-lines active)
       (map #(parse-line %))
       (flatten)
       (map #(Integer/parseInt %))
       (partition 6)
       ))



;; fill in the missing points between each given pair
(defn make-cuboid [cube]
  (remove empty?
          (conj pair 
                (let [x1 (first (first pair))
                      y1 (second (first pair))
                      x2 (first (second pair))
                      y2 (second (second pair))]
                  (if (= x1 x2)
                    (if (> y2 y1)
                      ;; fill y's
                      (for [i (range (inc y1) y2)]    [x1 i])
                      (for [i (range (dec y1) y2 -1)] [x1 i]))

                    ;; otherwise, fill x's
                    (if (> x2 x1)
                      (for [i (range (inc x1) x2)]    [i y1])
                      (for [i (range (dec x1) x2 -1)] [i y1]))
                    ))))
  )

(def cuboid
  (vec
   (for [z (range 0 15)]
     (vec
      (for [y (range 0 15)]
        (vec
         (for [x (range 0 15)]
           0)))))))

(assoc-in cuboid [1 1 1]  5)

(for [p parsed]
  (let [x1 (nth p 0)
        x2 (nth p 1)
        y1 (nth p 2)
        y2 (nth p 3)
        z1 (nth p 4)
        z2 (nth p 5)
        ]
    (for [x (range x1 (inc x2))
          y (range y1 (inc y2))
          z (range z1 (inc z2))]
      (assoc-in cuboid [x y z] 1))))


(loop [p parsed
       c cuboid]
  (if (empty? p) c
      (let [pi (first parsed)]
        (loop [xi (nth pi 0)
               ci c]
          (if (> xi (nth pi 1)) ci
              (recur (inc xi) (asso)))
          ))
      ))

;; If I'm `loop`ing, on x, how do I handle the other axis... y and z?
;; if I'm `for`ing, how do I preserve the new cuboid through each iteration?






(defn part1 []

  )




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= (part1) 39))
  (test/is (= (part2) 0)))

(run-tests)

