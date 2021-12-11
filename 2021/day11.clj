(ns aoc2021
  (:require [clojure.string :as string]))


(def day "11")
(def input-file (str "day" day "-input.txt"))

(def small "5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))


(def energies
  (vec
   (flatten
    (for [o 
          (->> active
               (string/split-lines)
               (map #(split #"" %)))]
      (map #(Integer/parseInt %) o)))))


;; (def width (count (first energy)))
;; (def height (count energy))
(def width 10)
(def height 10)

;; directions like on a clock: 1:00, 3:00, 5:00, 6:00 etc.
(def directions [(inc (- width)) 1 (inc width) width (dec width) -1 (dec (- width)) -10])

(defn increase-energy [energy]
  (mapv inc energy))


(defn valid-targets [pos]
  "return a list of flashable targets"
  (vec
   (let [d1 (->> directions
                 (map #(+ pos %))
                 (remove neg?)          ; 12 and some 11 and 1
                 (remove #(< (dec (* width height)) %)) ;;6 and some 5 and 7
                     
                 )]
     (if (zero? (mod pos width))
       ;; remove positions beyond the left edge
       (remove #(= (+ pos 9) %)
               (remove #(= (- pos 11) %) 
                       (remove #(= (dec pos) %) d1)))
       ;; remove positions beyond the right edge
       (if (= 9 (mod pos 10))
         (remove #(= (- pos 9) %)
                 (remove #(= (inc pos) %)
                         (remove #(= (+ pos 11) %) d1)))
         d1)))))





(defn flash-oct [pos energy]
  (for [i (range (* width height))]
    (let [e (nth energies i) []]
      (if (=> e 10)
        (assoc energy i 0 ))))

  )

;; continue working on flash-oct
;; keep everything working on vectors so we can use assoc to change energies
;; flash-oct will change the pos from 10 to 0 and inc the energies around it
;;   that are not already 0.

;; pseudocode
;; increment all the energies
;; iterate through looking for any 10's
;; . change the 10 to a 0
;; . inc any energies around this position that are not already 0
;; repeat this a specified (100) number of times.
;; 
;; read the problem statement again. We are supposed to be counting flashes
;; along the way. But I think it is more than just counting 0's in each 
;; step/iteration/phase.
;; 

;;1:45 so far.




(defn part1 []

  )




(defn part2 []

  )


(println "Part1: " (part1) "\nPart2: " (part2))



(deftest sample-data-test
  (clojure.test/is (= (part1) 1656))
  (clojure.test/is (= (part2) 00)))

(run-tests)

