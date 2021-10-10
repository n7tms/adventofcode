(ns adventofcode
  (:require [clojure.string :as str]))



;; I'm pretty proud of myself.  The part 1 solution is all me. If many of the previous
;; days, I relied on Fred Overflow for some assistance -- if not the all out solution.

(def input-file "day10-input.txt")
;(def input-file "day10-input-b.txt")   ;; small test data sample

(defn to-ints [x]
  (Integer/parseInt x))

(def numbers-working
  "convert a 'string' of numbers to a vector of sorted 'integers'"
  (->> input-file
       slurp
       str/split-lines
       (mapv to-ints)
       sort
  )
)

(def numbers
  "convert a 'string' of numbers to a vector of sorted 'integers'"
  (let [x 
        (->> input-file
             slurp
             str/split-lines
             (mapv to-ints)
             sort
             )]
    (sort (conj (conj x 0) (+ (last x) 3)))) ;; add a zero and "my adapter" (max + 3) to the list
)


(defn ones-and-threes [nums]
  "count the number of differences (1's and 3's) in a list of numbers"
  (loop [idx 1
         ones   0
         threes 0]
    (cond
      (= idx (count nums)) [:ones ones :threes threes] 
      (= 1 (- (nth nums idx) (nth nums (dec idx)))) (recur (inc idx) (inc ones) threes)
      (= 3 (- (nth nums idx) (nth nums (dec idx)))) (recur (inc idx) ones (inc threes))
      )
    ))  ;; => [:ones 66 :threes 29]

(ones-and-threes numbers)

(defn part1 []
  "produce the solution to part 1"
  (let [x (ones-and-threes numbers)]
    (* (nth x 1) (nth x 3))) 
)

(part1)  ;; => 1914

;; I got stuck on part 2. The following code is Fred Overflow's.

(def small [16 10 15 5 1 11 7 19 6 12 4])
(def medium [28 33 18 42 31 14 46 20 48 47 24 23 49 45 19 38 39 11 1 32 25 35 8 17 7 9 4 2 34 10 3])
(def large
  "convert a 'string' of numbers to a vector of sorted 'integers'"
  (->> input-file
       slurp
       str/split-lines
       (mapv to-ints)
       sort
       )
)

(defn deltas [joltages]
  (->> joltages
       (apply max)
       (+ 3)
       (conj joltages 0)
       sort
       (partition 2 1)
       (mapv (fn [[a b]] (- b a))))
)

;; I have broken down the above fn in an attempt to understand it better. (fail!)
(->> small                  ;; => [16 10 15 5 1 11 7 19 6 12 4]
       (apply max)          ;; => 19    !?! Where did the vector go??!?
       (+ 3)                ;; => 22    !?! ditto!?!
       (conj small 0)       ;; => [16 10 15 5 1 11 7 19 6 12 4 0 22]    ??? How did the vector come back into play??
       sort                 ;; => (0 1 4 5 6 7 10 11 12 15 16 19 22)
       (partition 2 1)      ;; ((0 1) (1 4) (4 5) (5 6) (6 7) (7 10) (10 11) (11 12) (12 15) (15 16) (16 19) (19 22))
       (mapv (fn [[a b]] (- b a)))  ;; => [1 3 1 1 1 3 1 1 3 1 3 3]
       )

;; Fred's part 1
(let [{ones 1, threes 3} (frequencies (deltas large))]
  (* ones threes))  ;; => 1914

;; Now that I have working Fred code, I can implement his part 2.

(defn count-ways
  ([x] 1)
  ([x y & zs]
   (if (> (+ x y) 3)
     (apply count-ways y zs)
     (+
      (apply count-ways y zs)
      (apply count-ways (+ x y) zs)))))

(apply count-ways (deltas small))  ;; =>     8
(apply count-ways (deltas medium)) ;; => 19208

;; Need to use the memoize form in order to handle the depth of the recursion
;; Notice the def function rather than a defn used above.
(def count-ways-m
  (memoize
   (fn
     ([x] 1)
     ([x y & zs]
      (if (> (+ x y) 3)
        (apply count-ways-m y zs)
        (+
         (apply count-ways-m y zs)
         (apply count-ways-m (+ x y) zs)))))))

(apply count-ways-m (deltas large))  ;; => 9256148959232


