;; Advent of Code 2020
;; Day 03
;;
;; https://adventofcode.com/2020/day/3
;;
;; Description of problem
;; Navigate a forest map 3-right, 1-down counting trees "#".
;;

(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def sample "..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#")

;; This is the input data we'll be using.
(def input-file "day03-input.txt")

;; This slurp line is just for testing. (Can I read the input file?)
;(slurp input-file)

;;=======================================================================
;; Here are two ways of doing essentially the same thing:
;;    1. Use a reader to read the file.
(defn read-lines [filename]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         vec)))

;;    2. Use slurp and split the lines. 
(defn read-lines-alt [filename]
  (str/split-lines (slurp input-file)))
;; One of the main difference I'm aware of is that slurp loads the entire file into memory.
;;=======================================================================


;; We can use either of the two methods above to get our terrain into the system.
;(def terrain (read-lines input-file))      ;; reader method
(def terrain (read-lines-alt input-file))   ;; slurp method



;; The following let returns (["....#...#####..##.#..##..#....#" 0], ...)
;; notice the 0 at the end. For this line, the first line, this is our position on this line.
;; The next line of terrain would have a 3, and the following line, a 6, and so on.
;
;(let [width (count (nth terrain 0)) columns (iterate #(mod (+ 3 %1) width) 0)] (map vector terrain columns)  )

(defn part1 []
  (let [width (count (nth terrain 0))                ;; deterimine the width of the terrain => 31
        columns (iterate #(mod (+ 3 %1) width) 0)    ;; get a list of columns we'll hit...slope 3:1, every third column.
        positions (map nth terrain columns)          ;; get the character (space or tree) present at that column
        trees (filter #(= \# %1) positions)]         ;; get a list of just the trees in those positions
    (count trees)                                    ;; return the count of how many trees
  )
)

(defn navigate [terrain steps]                       ;; We modified the part1 function to accept additional parameters,
                                                     ;;    the terrain and the steps right, so we can abstract the slope.
  (let [width (count (nth terrain 0))                ;; deterimine the width of the terrain => 31
        columns (iterate #(mod (+ steps %1) width) 0);; get a list of columns we'll stop on...depending on the slope.
        positions (map nth terrain columns)          ;; get the character (space or tree) present at that column
        trees (filter #(= \# %1) positions)]         ;; get a list of just the trees in those positions
    (count trees)                                    ;; return the count of how many trees
  )
)

;; Using our new navigate function, we can easily calculate the number of trees we hit on a variety
;; of different slopes.... 1:1, 3:1, 5:1, 7:1, and 1:2.
(defn part2 []
  (*  (navigate terrain 1)
      (navigate terrain 3)
      (navigate terrain 5)
      (navigate terrain 7)
      (navigate (take-nth 2 terrain) 1)))

(part1)    ;; => 244
(part2)    ;; => 9406609920
