(ns aoc
  (:require [clojure.string :as str]))

(defn inrange [x l h]
  (and (>= (compare x l) 0) (<= (compare x h) 0)))

(defn htb [x]
  (let [ix (int x)
        val (cond
              (inrange x \0 \9) (- ix (int \0))
              (inrange x \A \F) (+ 10 (- ix (int \A)))
              (inrange x \a \f) (+ 10 (- ix (int \a))))
        ] (loop [i 0 n val bits '()]
            (if (< i 4)
              (recur (inc i) (bit-shift-right n 1) (cons (bit-and n 1) bits))
              bits))))

(defn btn [b]
  (reduce #(+ (* 2 %1) %2) 0 b))

(defn splice [n x]
  (let [[a b] (split-at n x)] [(btn a) b]))

(defn intify [f] (fn [& args] (if (apply f args) 1 0)))

(def funcs `{0 + 1 * 2 min 3 max 5 (intify >) 6 (intify <) 7 (intify =)})

(defn parse-header [p]
  (let [[v r] (splice 3 p)
        [t r] (splice 3 r)
        ] [{:v v :t t} r]))

(defn parse-literal [p]
  (loop [r p n 0] 
    (let [ind (first r) 
          [b new-r] (splice 4 (rest r))
          new-n (+ (* 16 n) b)
          ] (if (= ind 0) [new-n new-r] (recur new-r new-n)))))

(declare parse-packet)

(defn parse-operator [p]
  (let [ind (first p)]
    (if (= ind 0)
      (let [[len r] (splice 15 (rest p))
            [ps r] (split-at len r)]
        (loop [ps ps x []] (if (empty? ps) [x r] (let [[a b] (parse-packet ps)] (recur b (conj x a))))))
      (let [[len r] (splice 11 (rest p))]
        (loop [r r x [] i 0] (if (>= i len) [x r] (let [[a b] (parse-packet r)] (recur b (conj x a) (inc i)))))))))

(defn parse-packet [p]
  (let [[{:keys [t]} r] (parse-header p)]
    (if (= t 4)
      (let [[n r] (parse-literal r)] [n r])
      (let [[ps r] (parse-operator r)] [(cons (get funcs t) ps) r]))))

(defn -main []
  (let [inp (slurp "day16-input.txt")
        bits (mapcat htb (str/trim inp))
        parsed (first (parse-packet bits))
        ans (eval parsed)
        ]
    (println ans)))

(-main)

;; part2  => 5390807940351

