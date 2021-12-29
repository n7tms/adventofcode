(ns aoc2021
  (:require [clojure.string :as string]))


;; small
;(def target-area {:minx 20, :miny -10, :maxx 30, :maxy -5})

;; large
(def target-area {:minx 14, :miny -267, :maxx 50, :maxy -225})

(defn in-target? [[x y]]
  (let [nx (:minx target-area)
        ny (:miny target-area)
        xx (:maxx target-area)
        xy (:maxy target-area)]
    (and (<= nx x xx) (<= ny y xy))))


(defn next-step [[[px py] [vx vy]]]
"arg: current pos; returns next pos based on rules.
 (inc x)
 "
  ;; px increases by vx; (if vx>0, then dec vx)
  ;; py increased by vy; (then dec vy)

  (let [npx (+ px vx)
        npy (+ py vy)
        nvx (if (> vx 0) (dec vx) 0)
        nvy (dec vy)]
    [[npx npy] [nvx nvy]])
  )


(defn part1 []
  (apply max
         (remove nil?
                 (for [x (range (inc (:maxx target-area)))
                       y (range 1000)]
                   (loop [pos [[0 0] [x y]]
                          height 0]
                     (let [cx (first (first pos))
                           cy (second (first pos))]
                       (if (or (> cx (:maxx target-area)) (< cy (:miny target-area)))
                         nil
                         (if (in-target? [cx cy])
                           height
                           (recur (next-step pos) (if (> cy height) cy height))))))))))


(defn part2 []
  (count
   (distinct
    (remove nil?
            (for [x (range (inc (:maxx target-area)))
                  y (range -1000 1000)]
              (loop [pos [[0 0] [x y]]
                     height 0]
                (let [cx (first (first pos))
                      cy (second (first pos))]
                  (if (or (> cx (:maxx target-area)) (< cy (:miny target-area)))
                    nil
                    (if (in-target? [cx cy])
                      [x y]
                      (recur (next-step pos) (if (> cy height) cy height)))))))))))



(println "Part1: " (part1) "\nPart2: " (part2))

;(part1)   ;; => 35511
;(part2)   ;; =>  3282


;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) xx))
;;   (test/is (= (part2) xx)))

;; (run-tests)

