;; Advent Of Code
;; Day 15 - Rambunctious Recitation
;;
;; Part 1 is all me.
;; My part 2 solution was going to take about 15 days play out. So, 
;; I borrowed Fred Overflow's part 2 solution.
;;

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

;(time (part1 start-seq))
;; => 276 in 93 sec
;; by my calculations, it will take 15 days to crunch through to a solution for part 2.


(defn starting [^String input]
  (->> input
       (re-seq #"\d+")
       (map-indexed (fn [^long index, ^String number]
                      [(Integer/parseInt number) (inc index)]))))

(defn part2 [the-input]
  (loop [spoken (into {} (starting the-input))
         number 0
         turn (inc (count spoken))]
    (if (= 30000000 turn)
      number
      (recur
       (assoc spoken number turn)
       (- turn (get spoken number turn))
       (inc turn)
       ))))

(part2 large)  ;; => 31916 in 44 seconds.
