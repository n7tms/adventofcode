(ns aoc2021
  (:require [clojure.string :as string]))


(def day "09")
(def input-file (str "day" day "-input.txt"))

(def small "2199943210
3987894921
9856789892
8767896789
9899965678")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(def parsed1
  (->> active
       (string/split-lines)
       (map #(split #"" %))))

(def width (count (first parsed1)))

(def parsed2
  (map #(Integer/parseInt %) (flatten parsed1)))


(defn ab [idx]
  (let [target (- idx width)]
    (if (> 0 target)
      11
      (nth parsed2 (- idx width)))))

(defn be [idx]
  (let [target (+ idx width)]
    (if (> target (dec (count parsed2)))
      11
      (nth parsed2 (+ idx width))))  )

(defn le [idx]
  (let [target (- idx 1 )]
    (if (< target (- idx (mod idx width)))
      11
      (nth parsed2 (dec idx))))   )

(defn ri [idx]
  (let [target (+ idx 1 )]
    (if (> target (+ (dec width) (- idx (mod idx width))))
      11
      (nth parsed2 (inc idx))))   )



(defn lowest? [i v]
  "determine if any neighbors are lower in value, i index, v value"
  (let [u (ab i)
        d (be i)
        l (le i)
        r (ri i)]
    (and (< v u) (< v d) (< v l) (< v r))))

(defn part1 []
  (reduce +
          (map inc
               (remove #(> 0 %)
                       (for [n (range (count parsed2))]
                         (let [v (nth parsed2 n)]
                           (if (lowest? n v) v -1))))))
  )  ;; => 494 in 59 minutes



(defn nine-ri
  ([i] (nine-ri i 0))
  ([i cnt]
   (if (< (ri i) 9)
     (+ (nine-ri (inc i) (inc cnt))
        (nine-be (+ i width) cnt))
     1)) 
)


(defn nine-be
  ([i] (nine-be i 0))
  ([i cnt]
   (if (< (be i) 9)
     (+ (nine-be (+ i width) (inc cnt))
        (nine-ri (inc i) cnt))
    1)) 
)

(nine-ri 0 0)
(nine-be 10 0)

(nine-ri 1 1)
(nine-ri 1 0)
(nine-ri 2 0)
(nine-ri 3 0)
(ri 0)



(defn part2 []
  (reduce *
          (take 3
                (sort #(compare %2 %1)
                      (for [n (range 12)]
                        (if (= (nth parsed2 n) 9)
                          0
                          (reduce + (flatten (list (nine-ri n) (nine-be n)))))
                        ))))

  )


(println "\nPart1: " (part1) "\nPart2: " (part2) "\n")


;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) xx))
;;   (test/is (= (part2) xx)))

;; (run-tests)

2:14:55 so far

