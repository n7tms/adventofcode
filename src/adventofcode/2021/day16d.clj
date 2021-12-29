(ns aoc2021.day16
  (:require
;   [aoc2021.util :as util]
   [clojure.pprint :as pp]
   [clojure.string :as str]
   [com.rpl.specter :as crs]
   [hashp.core]))

(def print-sexp false)

(defn parse
  [input]
  (let [text (str/trim (input))]
    (->> (str/split text #"")
         (map (fn [hex]
                (let [binary (-> hex
                                 (Long/parseLong 16)
                                 (Long/toString 2))]
                  (pp/cl-format nil "~4,'0d" binary))))
         (apply str))))

(defn seq->long
  [v]
  (Long/parseLong (apply str v) 2))

(declare process)

(defn process-literal
  [data]
  (loop [binary                []
         [stop a b c d & data] data]
    (let [binary' (conj binary a b c d)]
      (if (= stop \0)
        {:value [(seq->long binary')]
         :data  data}
        (recur binary' data)))))

(defn process-operator-bits
  [data]
  (let [[b data]       (split-at 15 data)
        length         (seq->long b)
        [packets data] (split-at length data)]
    (loop [result  []
           packets packets]
      (if (seq packets)
        (let [processed (process packets)]
          (recur (conj result processed) (:data (meta processed))))
        {:value result
         :data  data}))))

(defn process-operator-count
  [data]
  (let [[b data] (split-at 11 data)
        n        (seq->long b)]
    (loop [result  []
           x       0
           packets data]
      (if (= x n)
        {:value result
         :data  packets}
        (let [processed (process packets)]
          (recur (conj result processed)
                 (inc x)
                 (:data (meta processed))))))))

(defn process-operator
  [data]
  (let [[length-id data] (split-at 1 data)]
    (case length-id
      [\0] (process-operator-bits data)
      [\1] (process-operator-count data))))

(defmulti apply-op* :code)

(defn def-op
  [code op]
  (defmethod apply-op* code
    [{:keys [value]}]
    (apply list op value)))

(def-op 0 '+)
(def-op 1 '*)
(def-op 2 'min)
(def-op 3 'max)
(def-op 4 'identity)

(defn def-cond
  [code op]
  (defmethod apply-op* code
    [{:keys [value]}]
    (list 'if (apply list op value) 1 0)))

(def-cond 5 '>)
(def-cond 6 '<)
(def-cond 7 '=)

(defn apply-op
  [m]
  (with-meta (apply-op* m) m ))

(defn process
  [data]
  (let [[v data] (split-at 3 data)
        [t data] (split-at 3 data)
        version  (seq->long v)
        code     (seq->long t)
        result   (if (= code 4)
                  (process-literal data)
                  (process-operator data))]
    (apply-op (assoc result :version version :code code))))

(def METAS
  (crs/recursive-path [] p
                      (crs/if-path list?
                                   (crs/multi-path [crs/META (crs/must :version)]
                                                   [crs/ALL p]))))

(defn sum-versions
  [sexp]
  (reduce + (crs/select METAS sexp)))

(defn process-input
  [input]
  (let [sexp (-> input
                 parse
                 process)]
    (when print-sexp
      (pp/pprint sexp))
    sexp))

(defn part1
  [input]
  (-> input
      process-input
      sum-versions))

(defn part2
  [input]
  (-> input
      process-input
      eval))

;; (def input
;;   (util/input 16))

(def input (slurp "day16-input.txt"))
