;; Advent of Code
;; Day 17 - Conway Cubes
;;
;; Credit for the solution goes to Fred Overflow
;;

(ns adventofcode
  (:require [clojure.string :as string]))

(def small ".#.
..#
###")
(def large (slurp "day17-input.txt"))

;; using a 1-dimensional array to represent a 3-dimensional array
;; the maximum size that our initial 8x8 grid can grow to in six turns
;; is 32x32x32 => 32768.
(defn initial-grid ^bytes [^String input]
  (let [stride (.indexOf input "\n")
        padding (- 32 stride)]
    (-> input
        (string/escape {\. "\0"
                        \# "\1"
                        \newline (String. (byte-array padding))})
        .getBytes
        (java.util.Arrays/copyOf 32768))))

(def directions
  (vec
   (for [dz [-1024 0 +1024]
         dy [-32 0 +32]
         dx [-1 0 +1]
         :let [dir (+ dx dy dz)]
         :when (not (zero? dir))] ;; exclude the 0's (the current position)
     dir)))

;; - If a cube is active and exactly 2 or 3 of its neighbors are also active, 
;; the cube remains active. Otherwise, the cube becomes inactive.
;; - If a cube is inactive but exactly 3 of its neighbors are active, the cube
;; becomes active. Otherwise, the cube remains inactive.
(defn next-grid ^bytes [^bytes grid]
  (let [result (byte-array 32768)]
    (loop [pos 0]
      (if (= 32768 pos)
        result
        (let [x (aget grid pos)
              n (reduce
                 +
                 0
                 (map #(->> % (+ pos) (bit-and 32767) (aget grid)) directions))
              y (case x
                  1 (if (#{2 3} n) 1 0)
                  0 (if (= 3 n) 1 0))]
          (aset-byte result pos y)
          (recur (inc pos))))))  )

(defn part1 [input]
  (time (->> input
             initial-grid
             next-grid
             next-grid
             next-grid
             next-grid
             next-grid
             next-grid
             (reduce +))))

;;(part1 large)
;; => 304  (in 580 msec)

;; =====================================================================
;; Part 2 is basically the same code, just with a fourth dimension!

;; using a 1-dimensional array to represent a 3-dimensional array
;; the maximum size that our initial 8x8 grid can grow to in six turns
;; is 32x32x32x32 => 1048576.

(defn initial-grid ^bytes [^String input]
  (let [stride (.indexOf input "\n")
        padding (- 32 stride)]
    (-> input
        (string/escape {\. "\0"
                        \# "\1"
                        \newline (String. (byte-array padding))})
        .getBytes
        (java.util.Arrays/copyOf 1048576))))

(def directions
  (vec
   (for [dw [-32768 0 +32768]
         dz [-1024 0 +1024]
         dy [-32 0 +32]
         dx [-1 0 +1]
         :let [dir (+ dx dy dz dw)]
         :when (not (zero? dir))] ;; exclude the 0's (the current position)
     dir)))

;; - If a cube is active and exactly 2 or 3 of its neighbors are also active, 
;; the cube remains active. Otherwise, the cube becomes inactive.
;; - If a cube is inactive but exactly 3 of its neighbors are active, the cube
;; becomes active. Otherwise, the cube remains inactive.
(defn next-grid ^bytes [^bytes grid]
  (let [result (byte-array 1048576)]
    (loop [pos 0]
      (if (= 1048576 pos)
        result
        (let [x (aget grid pos)
              n (reduce
                 +
                 0
                 (map #(->> % (+ pos) (bit-and 1048575) (aget grid)) directions))
              y (case x
                  1 (if (#{2 3} n) 1 0)
                  0 (if (= 3 n) 1 0))]
          (aset-byte result pos y)
          (recur (inc pos))))))  )

(defn part2 [input]
  (time (->> input
             initial-grid
             next-grid
             next-grid
             next-grid
             next-grid
             next-grid
             next-grid
             (reduce +))))

(part2 large)   ;; => 1868 in 48 seconds



