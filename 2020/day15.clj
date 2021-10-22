(ns adventofcode)


(def small "0,3,6")                ;; => 436
(def small3 "0,3,6,0,3,3,1")                ;; => 436
(def small2 "0,3,6,0,3,3,1,0,4,0")
(def large "0,13,16,17,1,10,6")

(def start-seq (mapv #(Integer/parseInt %) (re-seq #"\d+" large)))

(defn in? [coll elm]
  (some #(= elm %) coll))

(defn next-number [x]
  (if (in? (butlast x) (last x))
    (- (count x) (inc (last (keep-indexed #(if (= (last x) %2) %1) (butlast x)))))
    0)
)


(defn part1 [begin]
  (loop [x begin]
    (if (= 2020 (count x))
      (last x)
      (recur (conj x (next-number x))))))

(time (part1 start-seq))
;; => 276 in 93 sec
;; by my calculations, it will take 15 days to crunch through to a solution for part 2.






