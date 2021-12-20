(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))


(def day "20")
(def input-file (str "day" day "-input.txt"))

(def small "")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))


(defn parse-input [input]
  (let [[_ enh-alg image]
        (re-matches #"(\w+)\n\n(\w+)" input)])
  {:enh-alg :image})

;; I might not need these.
(def enh-alg (:enh-alg (parse-input active)))
(def image   (:image   (parse-input active)))



(defn expand [image]
  "pad the top/bottom with ':' and the sides with '..' "
  (let [row-of-dots (repeat "." (count (first image)))
        img1 (cons row-of-dots image)        ;; two rows of dots on the top
        img2 (cons row-of-dots img1)
        img3 (conj row-of-dots img2)         ;; two rows of dots on the bottom
        img4 (conj row-of-dots img3)
        img5 (map #(str ".." % "..") img4)]  ;; two dots on both ends of each line
    img5))

(defn enhance-pixel [[col row] image]
  (let [neighbors [[-1 -1] [0 -1] [1 -1] [-1 0] [0 0] [1 0] [-1 1] [0 1] [1 1]]
        ;; build a string of the . and # from the neighbors
        ;; convert the . to 0 and # to 1
        ;; convert the binary number to decimal
        ;; return the nth from the :enh-alg
        ]
    )
  )


(defn enhance-image [image]
  (let [e-image (expand image)
        width   (dec (count (first e-image)))
        height  (dec (count e-image))]
    (for [col (range 1 width)
          row (range 1 height)]
      (enhance-pixel))))
;; this is probably going to come out as individual characters. get each row back into one string.



(defn count-light-pixels [image]
  (reduce + (map #(count) (map #(filter "#" %) image))) ;; maybe use `flatten` and `string/join` to get just one string of characters
  )


(defn part1 []
  (loop [x 2
         image (:image (parse-input active))]
    (if (zero? x)
      (count-light-pixels image)
      (recur (dec x) (enhance-image image)))))




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= (part1) 0))
  (test/is (= (part2) 0)))

(run-tests)

