(ns adventofcode
  (:require [clojure.string :as string]))

(def day "06")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN")

(def large (slurp input-file))

;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))

;; =============================================================================
;; adopted from ...
;; https://github.com/Turmolt/Advent-of-Code/blob/master/Clojure/2019/day6.clj
(def input
  (->> active
       string/split-lines
       (map #(split #"\)" %))
       (map (comp vec reverse))
       (into {})))

(defn build-chain [d k]
  (take-while identity (rest (iterate d k))))

(defn part1 []
  (->> (keys input)
       (map (comp count (partial #(build-chain input %))))
       (reduce +)))


(defn part2 []  
  (let [y (build-chain input "YOU")
        s (build-chain input "SAN")
        i (clojure.set/intersection (set y) (set s))]
    (->> i
         (map #(+ (.indexOf y %) (.indexOf s %)))
         (apply min))))
;; =============================================================================




;; =============================================================================
;; kickopotomus from Aoc solution sub-reddit
(defn ocount [g n]
  (loop [c 0 cn n]
    (if (= cn "COM")
      c
      (recur (inc c) (get g cn)))))

(defn part3 [data]
  (let [orbits (string/split-lines data)
        graph (reduce (fn [m o]
                        (let [[p c] (string/split o #"\)")]
                          (assoc m c p)))
                      {}
                      orbits)]
    (reduce + (map #(ocount graph %) (keys graph)))))

(part3 large)

;; =============================================================================



(defn bld-orbs [mp inp]
  (let [[p c] (split #"\)" inp)] (assoc mp c p)))

(let [orbits (string/split-lines active)
      graph (bld-orbs {} orbits)]
  (reduce + (map #(ocount graph %) (keys graph))))

(+ 3 2)


(bld-orbs {} (string/split-lines active))
