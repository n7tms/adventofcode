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

;(def active small)
(def active large)


(defn parse-p [f]
"helper fn for parse-pairs that matches the pair and the element"
  (let [[_             adj    elem]
        (re-matches #"(\w+) -> (\w)" f)]
    [adj elem]))

(defn parse-pairs [pairs]
"given the pairs from the input (:pairs input), returns a hash-map of all the 
pairs and the transformation letter."
  (let [ps
        (->> (string/split-lines pairs)
             (map #(parse-p %)))]
    (into {}
          (for [p ps]
            [(keyword (first p)) (second p)]))))


(def input
"divides the initial input into the template part and the polymer pairs part"
  (let [[template pairs] (string/split active #"\n *\n")]
    {:template (string/split template #"")
     :pairs  pairs}))


(defn lookup-element [pair]
"given a polymer pair, returns the letter that should be inserted between them"
  (pair (into {} (parse-pairs (:pairs input)))))

(def buckets
"creates an initialized buckets hash-map that contains polymer pairs
all with values of zero. This provides a blank slate for each step of 
transformations."
  (let [names (keys (parse-pairs (:pairs input)))]
    (apply hash-map (reverse (conj (interpose 0 names) 0)))))

(defn initialize-buckets [polymer buckets]
"populates a bucket with the starting polymer sequence pairs"
  (let [ps (map #(keyword %) (map #(string/join %) (partition 2 1 polymer)))]
    (loop [b buckets
           p ps]
      (if (empty? p)
        b
        (recur  (update b (first p) + 1) (rest p))))))


(defn insert-elements [prev-bucket]
;; iterate through the buckets
;; lookup what element results from the pair in the bucket
;; inc the resulting buckets (e.g. :NN => (inc :NC) and (inc :CN))
;;
  (loop [new-bucket buckets      ;; start with empty bucket
         elements   prev-bucket] ;; pairs to transform
    (if (empty? elements)
      new-bucket
      (let [e (key (first elements))  ;; :NN
            c (val (first elements))] ;; count of this element
        (if (zero? c)
          (recur new-bucket (rest elements))
          (let [t (lookup-element e)                        ;; "C"
                new1 (keyword (str   (subs (str e) 1 2) t)) ;; :NC
                new2 (keyword (str t (subs (str e) 2)    )) ;; :CN
                tmp (update new-bucket new1 + c)]
            (recur (update tmp new2 + c) (rest elements))))))))



(defn count-elements [bucket]
"reduces the final bucket (hash-map) to a map of letters (the first letter of
a pair (key))) and the number (val)"
  (for [b bucket]
    {(keyword (str (subs (str (key b)) 1 2))) (val b)}))


(defn merge-dups [map-list]
"count-elements contains duplicate keys with different values. merge-dups
combines the values into a set #{} and associates the set with a single key"
    (for [k (set (for [ml map-list] (key (first ml))))]
         {k (set (remove nil? (for [ml map-list] (k ml))))}))

(defn transform [polymer buckets steps]
  (loop [step 0
         bucket (initialize-buckets polymer buckets)]
    (if (= step steps)
      bucket
      ;; insert the elements
      (recur (inc step) (insert-elements bucket))
      )))


(defn cnt [steps]
  (let [hs
        (->> (transform (:template input) buckets steps)
             (count-elements)
             (merge-dups))
        
        last-elem (keyword (last (:template input)))

        x (sort
           (for [c hs]
             (if (= last-elem (key (first c)))
               (inc (reduce + (second (first c))))
               (reduce + (second (first c))))))]

    (- (last x) (first x))))



(defn part1 [] (cnt 10))
(defn part2 [] (cnt 40))

(time (part1))   ;; => 2168
(time (part2))   ;; => 2967977072188



