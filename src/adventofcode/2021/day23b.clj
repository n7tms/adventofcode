;; Advent of Code - Day 23
;; My first attempted to solve part 1 by hand. Several attempts kept producing the wrong answer.
;; I resolved to solve it using a path-finding algorithm -- like Dijkstra -- but never got to it.
;;
;; The following solution is adapted from https://github.com/wevre/advent-of-code/blob/master/src/advent_of_code/2021/day_23.clj
;;

(ns adventofcode
  (:require [clojure.data.priority-map :refer [priority-map-keyfn]]
            [clojure.string :as str]))


;;; Parameters
(def room-capacity 2)
(def room-col {:A 2 :B 4 :C 6 :D 8})

(defn print-burrow [{:keys [hall rooms]}]
  (doseq [x (range 11)]
    (print (if (contains? hall x) (or (some-> x hall name) \·) " ")))
  (println)
  (doseq [r (range room-capacity)]
    (print "  ")
    (apply print (map #(or (some-> (get-in rooms [% r] nil) name) \·) [:A :B :C :D]))
    (println)))

;;; Finding moves
(def energy {:A 1 :B 10 :C 100 :D 1000})

(defn hall-clear? [c1 c2 hall]
  (let [[c1 c2] (if (< c1 c2) [c1 c2] [c2 c1])]
    (->> hall
         (filter (fn [[col _]] (< c1 col c2)))
         (every? (fn [[_ pod]] (nil? pod))))))

(defn room-clear? [pod rooms] (every? #(or (nil? %) (= pod %)) (rooms pod)))

(defn moves [{:keys [rooms hall] :as burrow}]
  (let [move-outs
        (for [[k room] rooms
              :let [col (room-col k)
                    [vac [f & fs]] (split-with nil? room)]
              :when f
              :let [dest (room-col f)]
              :when (or (not= col dest) (apply not= f fs))]
          {:from-col col :out-dist (inc (count vac)) :pod f :vacate [:rooms k (count vac)]})
        move-ins
        (for [[col f] hall :when f] {:from-col col :pod f :vacate [:hall col]})]
    (concat
     ;; try each move-out to hallway spots
     (for [{:keys [from-col out-dist pod vacate]} move-outs
           to-col (keep (fn [[c f]] (when-not f c)) hall)
           :when (hall-clear? from-col to-col hall)]
       {:move/burrow (-> burrow
                         (assoc-in [:hall to-col] pod)
                         (assoc-in vacate nil))
        :move/cost (* (energy pod) (+ out-dist (Math/abs (- from-col to-col))))})
     ;; try pods to their home
     (for [{:keys [from-col out-dist pod vacate] :or {out-dist 0}} (concat move-ins move-outs)
           :let [to-col (room-col pod)
                 in-dist (count (filter nil? (rooms pod)))]
           :when (and (hall-clear? from-col to-col hall) (room-clear? pod rooms))]
       {:move/burrow (-> burrow
                         (assoc-in [:rooms pod (dec in-dist)] pod)
                         (assoc-in vacate nil))
        :move/cost (* (energy pod) (+ out-dist in-dist (Math/abs (- from-col to-col))))}))))


;;; Costs
(defn room-penalty [[k room]]
  (let [room (remove nil? room)]
    (->> (map room-col room)
         (map #(Math/abs (- (room-col k) %)))
         (map #(if (zero? %) 0 (+ 2 %)))
         (map * (map energy room))
         (reduce +))))

(defn hall-penalty [[col pod]]
  (if pod
    (let [dist (Math/abs (- col (room-col pod)))]
      (* (energy pod) (+ 1 dist)))
    0))

(defn penalty [burrow]
  (+
   (reduce + (map hall-penalty (:hall burrow)))
   (reduce + (map room-penalty (:rooms burrow)))))


(defn update-costs [{node-cost :node/cost}]
  (fn [costs {:move/keys [burrow cost]}]
    (if (contains? costs burrow)
      (update-in costs [burrow :node/cost] (fnil min ##Inf) (+ node-cost cost))
      (assoc costs burrow {:node/cost (+ node-cost cost) :node/penalty (penalty burrow)}))))


;; Solve
(defn organize [start]
  (let [end (into {} (map (juxt identity #(vec (repeat room-capacity %))) [:A :B :C :D]))]
    (loop [costs (priority-map-keyfn #(+ (:node/cost %) (:node/penalty %))
                                     start
                                     {:node/cost 0 :node/penalty (penalty start)})
           visited #{}]
      (let [[burrow cost] (peek costs)]
        (if (= (:rooms burrow) end)
          cost
          (let [moves (->> burrow moves (remove #(visited (:move/burrow %))))]
            (recur (reduce (update-costs cost) (pop costs) moves)
                   (conj visited burrow))))))))


(defn part1 []          
  (let [burrow {:hall {0 nil 1 nil 3 nil 5 nil 7 nil 9 nil 10 nil}
                :rooms {:A [:A :D] :B [:C :A] :C [:B :D] :D [:C :B]}}]
    (organize burrow)))

(defn part2 []
  (with-redefs [room-capacity 4]
    (let [burrow {:hall {0 nil 1 nil 3 nil 5 nil 7 nil 9 nil 10 nil}
                  :rooms {:A [:A :D :D :D] :B [:C :C :B :A] :C [:B :B :A :D] :D [:C :A :C :B]}}]
      (organize burrow))))


(part1)    ;; => 16300
(part2)    ;; => 48676
