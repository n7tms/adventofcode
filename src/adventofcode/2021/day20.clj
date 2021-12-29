(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))


(def day "20")
(def input-file (str "day" day "-input.txt"))

(def small "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(rest (split #"\n\n" active))
(rest (string/split-lines active))

(defn parse-input [input]
  (let [lines (split #"\n\n" input)
        enh-alg (first lines)
        image (string/split-lines (first (rest lines)))]
    [enh-alg image])
)


(def enh-alg (first  (parse-input active)))
(def image   (second (parse-input active)))


(defn expand [image]
  "pad the top/bottom with ':' and the sides with '..' "
  (let [row-of-dots (string/join (repeat (count (first image)) "."))
        img1 (cons row-of-dots image)        ;; two rows of dots on the top
        img2 (cons row-of-dots img1)
        img3 (conj (vec img2) row-of-dots)   ;; two rows of dots on the bottom
        img4 (conj img3 row-of-dots)
        img5 (map #(str ".." % "..") img4)]  ;; two dots on both ends of each line
    img5))


;; [x col y row]

(defn enhance-pixel [[col row] image]
(str
 (nth enh-alg 
      (Integer/parseInt
       (clojure.string/replace 

        (let [x col y row]
          (string/join
           (for [row [-1 0 1]
                 col [-1 0 1]]
             (let [px (+ x row)
                   py (+ y col)]
               (nth (nth image py) px)))))

        #".|#" {"." "0" "#" "1"}) 2))))




(defn enhance-image [image]
  (let [e-image (expand image)
        width   (dec (count (first e-image)))
        height  (dec (count e-image))]
    (for [row (range 1 height)]
      (string/join
       (for [col (range 1 width)]
         (enhance-pixel [row col] e-image))))))

;(enhance-image image)

(for [x 
      (enhance-image image)]
  (println x))

(for [x (expand image)]
  (println x))


(defn count-light-pixels [image]
  (reduce + (map #(count %) (map #(filter "#" %) image))) ;; maybe use `flatten` and `string/join` to get just one string of characters
  )


(defn part1 []
  (loop [x 1
         image (:image (parse-input active))]
    (if (zero? x)
      (count-light-pixels image)
      (recur (dec x) (enhance-image image)))))




(defn part2 []

  )



;;(println "Part1: " (part1) "\nPart2: " (part2))


;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) 0))
;;   (test/is (= (part2) 0)))

;; (run-tests)

