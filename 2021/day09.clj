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

;(def active small)
(def active large)

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

(declare nine-be)

(defn nine-ri
  ([i] (nine-ri i 0))
  ([i cnt]
   (println "r-i:" i " r-cnt:" cnt)
   (if (< (ri i) 9)
     (+ (nine-ri (inc i)  cnt)
        (nine-be (+ i width) cnt))
     1)) 
)

(defn nine-be
  ([i] (nine-be i 0))
  ([i cnt]
   (println "b-i:" i " b-cnt:" cnt)
   (if (< (be i) 9)
     (+ (nine-be (+ i width)  cnt)
        (nine-ri (inc i) cnt))
    1)) 
)



;; (defn part2 []
;;   (reduce *
;;           (take 1
;;                 (sort #(compare %2 %1)
;;                       (for [n (range 3)]
;;                         (if (= (nth parsed2 n) 9)
;;                           0
;;                           (do
;;                             (println "ri:" (nine-ri n) "  be:"(nine-be n))
;;                             (reduce + (flatten (list (nine-ri n) (nine-be n))))))
;;                         ))))

;;   )


;; create a 'variable' call basin
;; if the current point is not equal to 9,
;; . check if this point is already in a basin 
;; . . if not, create a new basin and add this point
;; . check if the neighbors (down and right) are in the basin
;; . . if not, add them to the basin that was just created
;; after going through all the points
;; . map reduce + to each basin
;; . sort descending
;; . reduce * take 3 from the sorted list
;; ANSWER!

(defn in-basin? [idx basins]
  (first
   (remove neg?
           (for [x (range (count basins))]
             (if (not (nil? (some #{idx} (nth basins x))))
               x
               -1)))))


(defn check-neighbors [idx basin]
  (distinct
   (flatten
    (list
     (if (and (< (ri idx) 9) (nil? (some #{(inc idx)} basin)))
       (conj basin (inc idx))
       basin)
     (if (and (< (be idx) 9) (nil? (some #{(+ idx width)} basin)))
       (list
        (conj basin (+ idx  width))
        (if (and (< (le (+ idx width)) 9) (nil? (some #{(+ idx (dec width))} basin)))
          (conj basin (+ idx (dec width)))
          basin)        )
       basin)
     ;; (if (and (< (le idx) 9) (nil? (some #{(dec idx)} basin)))
     ;;   (conj basin (dec idx))
     ;;   basin)

     ))))




(defn replace-basin [basin-idx basins new-basin]
  (for [b basins]
    (if (= b (nth basins basin-idx))
      new-basin
      b)))


(defn doit []
  (loop [idx 0
         basins '(())]
    (if (= idx (count parsed2))
      basins
      (let [point (nth parsed2 idx)]
        (if (= point 9)
          (recur (inc idx) basins)
          (let [basin (in-basin? idx basins)]
            (if (not (nil? basin))
              ;; if this point is in a basin, then check to see if the
              ;; neighbors are too. Check-neighbors should return a list.
              ;; CHANGE `replace` WITH (assoc basins basin (check-n...)) (requires vectors!
              (recur (inc idx) (replace-basin basin basins (check-neighbors idx (nth basins basin))))
;              (recur (inc idx) (assoc basins basin (check-neighbors idx (nth basins basin))))
              ;; if this point is not in a basin, then create a new basin
              ;;with this point in it.
              (recur (inc idx)  (conj basins (check-neighbors idx (conj '() idx))))
              ))
          ))
      )))


(def c '((2 1 0 100 101) ()))
(def idx 2)
(def point (nth parsed2 idx))
point
(def ib (in-basin? idx c))
(conj c (check-neighbors idx (conj '() idx)))

(replace-basin ib c (check-neighbors idx (nth c ib)))




(count (doit))
(reduce * (take 3 (sort #(compare %2 %1)  (map count (doit)))))

;(println "\nPart1: " (part1) "\nPart2: " (part2) "\n")



;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) xx))
;;   (test/is (= (part2) xx)))

;; (run-tests)

;; tried  551286; to low.
;;05:24:00 so far

;; my algorithm is not taking into account situations like:
;; 999911999
;; 993911199
;; 911111999
;; the 3 is not discovered as part of the basin it belongs in.




