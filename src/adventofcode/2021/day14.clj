(ns aoc2021
  (:require [clojure.string :as string]))




(def day "14")
(def input-file (str "day" day "-input.txt"))

(def small "NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C")

(def large (slurp input-file))

(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))


(defn parse-p [f]
  (let [[_             adj    elem]
        (re-matches #"(\w+) -> (\w)" f)]
    [adj elem])

  )

(defn parse-template [template]
  template
  )

(defn parse-pairs [pairs]
  (let [ps
        (->> (string/split-lines pairs)
             (map #(parse-p %)))]
    (for [p ps]
      [(string/split  (first p) #"") (second p)])
    )
  )

(def input
  (let [[template pairs] (string/split active #"\n *\n")]
    {:template (string/split (parse-template template) #"")
     :pairs (parse-pairs pairs)}))


(defn partition-polymer [polymer]
  (partition 2 1 polymer))

(defn get-element [pair]
  (first
   (remove nil?
           (for [p (:pairs input)]
             (when (= (first p) pair) (second p))))))

(def last-elem (str (last (:template input))))


[["N" "N"] "C"]
(defn insert-elements [polymer]
  (map str
       (flatten
        (cons 
         (for [p (partition-polymer polymer)]
           (let [e (get-element p)]
             (if (nil? e)
               p
               [(first p) e]))
           )  last-elem))))



(defn part1 [steps]
  (let [p 
        (loop [step steps
               polymer (:template input)]
          (if (= step 0) polymer
              (recur (dec step) (insert-elements polymer))))
        f (sort-by val (frequencies p))]
    (- (last (last f)) (last (first f))))
  )


(time (part1 1))   ;; => 2360 (52 minutes)


;; The length of the polymner at step 40 will be 20890720927745. It will
;; take 25.5 days to reach a solution ..... time for plan B!
;; This feels like lantern fish all over again. Can I use buckets here??






;(time (part2))



;; (println "Part1: " (part1) "\nPart2: " (part2))


;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (clojure.test/is (= (part1) 1588))
;;   (clojure.test/is (= (part2) 2188189693529)))

;; (run-tests)

