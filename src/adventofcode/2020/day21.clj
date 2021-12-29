;; Advent of Code
;; Day 21 - Allergen Assessment
;;
;; Solution compliments of Fred Overflow
;;

(ns adventofcode
  (:require [clojure.string :as string]))


(def small "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)")

(def large (slurp "day21-input.txt"))

(defn parse-foods [^String input]
  (for [line (string/split input #"\n *")]
    (let [[ _ ingredients allergens]
          (re-matches #"([\w ]+)\(contains ([\w, ]+)\)" line)]
      {:ingredients (set (string/split ingredients #" "))
       :allergens (set (string/split allergens #", "))})))

(defn possible-ingredients [foods]
  (for [allergen (apply clojure.set/union (map :allergens foods))]
    [allergen (apply clojure.set/intersection
                     (for [food foods
                           :when (contains? (:allergens food) allergen)]
                       (:ingredients food)))])

  )

(->> small parse-foods possible-ingredients)

(defn certain-ingredient [possible-ingredients]
  (loop [input possible-ingredients
         output {}]
    (if (empty? input)
      output
      (let [[[allergen ingredients] & more]
            (sort-by (fn [[allergen ingredients]] (count ingredients)) input)
            ingredient (first ingredients)]
        (recur
         (for [[allergen ingredients] more]
           [allergen (disj ingredients ingredient)])
         (assoc output allergen ingredient)))))
  )

(->> small parse-foods possible-ingredients certain-ingredient)


(defn part1 []
  (let [foods (parse-foods large)
        ingredients (apply concat (map :ingredients foods))
        certain (certain-ingredient (possible-ingredients foods))
        allergic (set (vals  certain))
        harmless (remove allergic ingredients)]
    (count harmless)
    ))

(part1)  ;; => 2428


(defn part2 []
  (->> large
       parse-foods
       possible-ingredients
       certain-ingredient
       sort
       vals
       (string/join ",")))

(part2)  ;; => bjq,jznhvh,klplr,dtvhzt,sbzd,tlgjzx,ctmbr,kqms

