(ns projecteuler.mylib.mylibrary)

;; for information on how to create/use a custom library, see
;; https://www.braveclojure.com/organization/


;; generates an infinite list of primes.
(defn primes []
  (letfn [(enqueue [sieve n step]
            (let [m (+ n step)]
              (if (sieve m)
                (recur sieve m step)
                (assoc sieve m step))))
          (next-sieve [sieve candidate]
            (if-let [step (sieve candidate)]
              (-> sieve
                (dissoc candidate)
                (enqueue candidate step))
              (enqueue sieve candidate (+ candidate candidate))))
          (next-primes [sieve candidate]
            (if (sieve candidate)
              (recur (next-sieve sieve candidate) (+ candidate 2))
              (cons candidate 
                (lazy-seq (next-primes (next-sieve sieve candidate) 
                            (+ candidate 2))))))]
    (cons 2 (lazy-seq (next-primes {} 3)))))



;; usage:
;; (primes) => returns an infinite list of prime numbers from 2 to infinite
;; (take 10 (primes)) => the first 10 prime numbers (2 3 5 7 11 13 17 19 23 29)
;; (last (take 10 (primes))) => find the 10'th prime number => 29
;; Prime solution from http://clj-me.cgrand.net/index.php?s=Primes


;; https://thesoftwaresimpleton.com/blog/2015/02/07/primes
(defn primes-ss [n]
  (let [root (-> n (Math/sqrt) inc int)
        sieve (boolean-array n true)]
    (loop [i 2]
      (when (< i (Math/sqrt n))
        (when (aget sieve i)
          (loop [j (* i 2)]
            (when (< j n)
              (aset sieve j false)
              (recur (+ j i)))))
        (recur (inc i))))
    (filter #(aget sieve %) (range 2 n))))

;; Usage:
;; (primes-ss 100)  => generates a list of primes less then 100
;; I suggest you use this to generate a "large" list of primes
;; (eg. (defn primes (primes-ss 1000000) ) and then work off 
;; that list.



;; Is a number a prime number
(defn prime? [n]
  (not-any? zero? (map #(rem n %) (range 2 n))))



;; This fibonacci implementation is more efficient -- only one recursion loop.
(defn fib [a b cnt]
  (if (zero? cnt)
    b
    (recur (+ a b) a (dec cnt)))) ;
;; usage: (map (partial fib 1 0) (range 10))  ; => (0 1 1 2 3 5 8 13 21 34)


(defn fibs []
  ((fn fib [a b]
     (lazy-seq (cons a (fib b (+ a b)))))
   0 1))
;; usage:
;; (fibs) => returns an infinite sequence (list) of fibonacci numbers, from 0 to infinite
;; (take 10 (fibs)) => the first 10 fibonacci numbers (0 1 1 2 3 5 8 13 21 34)
;; (last (take 10 (fibs))) => find the 10'th fibonacci number => 34

(def another-fib (map first (iterate (fn [[a b]] [b (+' a b)]) [0 1])))
;; usage:
;; (take 10 fib) => (0 1 1 2 3 5 8 13 21 34)



(defn factorial [n]
  "returns the factorial of the given positive integer argument"
  (if (= n 0) 1
      (loop [val n i n]
        (if (<= i 1) val
            (recur (*' val (dec i)) (dec i))))))
;;
;;
;;


(defn pascal-next-row [x]
  "returns the next row in a Pascal triangle sequence"
  (let [size (count x)]
    (for [i (range (inc size))]
      (if (or (= 0 i) (= size i)) 1
          (+ (nth x (dec i)) (nth x i))))))
;; usage:
;; (pascal-next-row [1 2 1]) => (1 3 3 1)
;; (take 5 (iterate pascal-next-row [1])) returns the first 5 Pascal triangle rows.
;;



(defn power
  "raise 'base' to the power of 'exp'"
  [base exp]
  (reduce * (repeat (bigint exp) (bigint base)))
  )



;; The following functions are part of a system to calculate the day of the week for any date
;; between 1700 and 2399, inclusive. 
;; Based on an algorithm published here: https://artofmemory.com/blog/how-to-calculate-the-day-of-the-week/
;;
;; Note: leap-year? can be used as a stand-alone function for ANY year.
(defn year-code
  "accepts a 2 or 4 digit year and returns the year-code"
  [y]
  (let [year (mod y 100)]
    (mod (+ year (quot year 4)) 7))
  )


(defn month-code [m]
  ([0 3 3 6 1 4 6 2 5 0 3 5] (dec m))
  )

(defn century-code
  "accepts a 4 digit year (1700-2399) and returns the century-code"
  [c]
  (let [cc (quot c 100)]
    (case cc
      17 4
      18 2
      19 0
      20 6
      21 4
      22 2
      23 0))
  )

(defn leap-year?
  "given a year, returns true if it is a leap year; otherwise, false"
  [y]
  (cond (zero? (mod y 400)) true
        (zero? (mod y 100)) false
        (zero? (mod y   4)) true
        :default false)
  )

(defn leap-year-code
  "given a month and year, returns -1 if Jan or Feb AND year is a leap year; otherwise, 0"
  [m y]
  (if (and (leap-year? y) (or (= m 1) (= m 2))) -1 0)
  )

(defn day-of-week
  "given a date, returns the day of the week, 0=Sun, 1=Mon, ... 6=Sat"
  [y m d]
  (mod (+ (year-code y) (month-code m) (century-code y) d (leap-year-code m y)) 7))

(def days ["Sunday" "Monday" "Tuesday" "Wednesday" "Thursday" "Friday" "Saturday"])

(defn day-of-week-name
  "given a date, returns the name of the day of the week"
  [y m d]
  (days (day-of-week y m d)))



;;
;; fn addem loops through the digits adding the digits to the previous
;; sum in the accumulator (acc).
;; If you were given a problem like:
;;    If 10! = 3628800, what are the sum of the digits,...
;;    e.g. 3+6+2+8+8+0+0 = 27
;; ... you provide the "3628800" (as a string) and add-digits will return 27.
(defn add-digits
  "given a 'string' of digits, add-digits returns the sum of the digits"
  [nums]
  (loop [acc 0 idx 0]
    (if (= idx (count nums))
      acc
      (recur (+ acc (Integer/parseInt (str (nth nums idx)))) (inc idx) )) 
    ))


;; produce a list of divisors of n, not including n
;; e.g. (divisors 10) => (1 2 5)
(defn divisors
  ""
  [n]
  (filter (comp zero? (partial rem n)) (range 1 n)))



(def triangle-nums   (map #(/ (* % (+ % 1)) 2) (iterate inc 1)))
(def square-nums     (map #(* % %) (iterate inc 1)))
(def pentagonal-nums (map #(/ (* % (- (* 3 %) 1)) 2) (iterate inc 1)))
(def hexagonal-nums  (map #(* % (- (* 2 %) 1)) (iterate inc 1)))
(def heptagonal-nums (map #(/ (* % (- (* 5 %) 3)) 2)  (iterate inc 1)))
(def octagonal-nums  (map #(* % (- (* 3 %) 2)) (iterate inc 1)))


(defn triangle? [value]
  (if (< value 0)
    false
    (let [c (* -2 value)
          b 1
          a 1
          d (- (* b b) (* 4 a c))]
      (if (< d 0)
        false
        (let [root1 (/ (+ (- b) (Math/sqrt d)) (* 2 a))
              root2 (/ (- (- b) (Math/sqrt d)) (* 2 a))]
          (if (and (> root1 0)
                   (= root1 (Math/floor root1)))
            true
            (if (and (> root2 0)
                     (= root2 (Math/floor root2)))
              true
              false))))))
  )


(defn pentagonal? [value]
  (zero? (rem (+ 1 (Math/sqrt (+ 1 (* 24 value)))) 6)))

(defn hexagonal? [value]
  (let [n (/ (+ (Math/sqrt (+ (* 8 value) 1)) 1) 4)]
    (if (= n (Math/floor n))
      true
      false))
  )


(defn abs [n]
  "given a number n, return the absolute value of n"
  (max n (- n)))


(defn same-digits?
  "compare the digits in num1 and num2; return true if they both contain the same digits"
  [num1 num2]
  (=
   (sort (mapv #(Integer/parseInt %) (clojure.string/split (str num1) #"" )))
   (sort (mapv #(Integer/parseInt %) (clojure.string/split (str num2) #"" )))
   )
  )


;; Determines if a sequence of digit (1 2 3 ... n) is pandigital...meaning it contains all
;; of the digits, 1-9, only once. 453267981 is pandigital. 46788321 is not.
;; This function could be easily modified to determine if a number is pandigital for 1-5 or 1-7, etc.
(defn to-digits [num]
  (map #(Character/getNumericValue %) (str num)))

(defn unique-digits? [digits]
  (let [unique (distinct digits)]
    (= (count unique) (count digits))))

(defn pandigital? [digits]
  "returns true if a seq of digits is pandigital-9"
  (and (nil? (some zero? digits))
       (= 9 (count digits))
       (unique-digits? digits)))


;; Find the prime factors of n
(defn factors-starting-at [f n]
  (cond
    (> f (Math/sqrt n)) (if (= n 1) [] [n])
    (= 0 (mod n f)) (cons f (factors-starting-at f (/ n f)))
    :else (recur (inc f) n)))

(defn prime-factors-of
  "returns a list of prime factors of n"
  [n]
  (factors-starting-at 2 n))



(defn lazy-accum [seq]
  "given a lazy sequence seq, returns a lazy sequence of the sums of elements up to each element in the sequence"
  (map first (iterate (fn [[sum s]]
                        [(+ sum (first s)) (next s)])
                      [(first seq) (rest seq)])))

;; Example: (make-seq-accumulator (primes))  ;; => (2 5 10 17 28 ...)


(defn accum [seq]
  "Returns a sequence of the sums of elements up to each element in seq"
  (loop [e (first seq)
         r (rest seq)
         sums [0]]
    (if (nil? e)
      (rest sums)
      (recur (first r) (rest r) (conj sums (+ e (last sums)))))))

;; Example: (accum '(1 2 3 4))  ;; => (1 3 6 10)



;; ====================================================================
;; Functions to traverse graphs, BFS and DFS
;; from http://dnaeon.github.io/graphs-and-clojure/
;; ====================================================================
 
;; graph is a `map` of nodes and the nodes adjacent to it
(def graph {:A [:B :C]
            :B [:A :X]
            :X [:B :Y]
            :Y [:X]
            :C [:A :D]
            :D [:C :E :F]
            :E [:D :G]
            :F [:D :G]
            :G [:E :F]})

(defn visited? [v coll] (some #(= % v) coll))

(defn find-neighbors [v coll] (get coll v))

(defn graph-dfs
  "Traverses a graph in Depth First Search (DFS)"
  [graph v]
  (loop [stack   (vector v) ;; Use a stack to store nodes we need to explore
         visited []]        ;; A vector to store the sequence of visited nodes
    (if (empty? stack)      ;; Base case - return visited nodes if the stack is empty
      visited
      (let [v           (peek stack)
            neighbors   (find-neighbors v graph)              ;; right branch first
;            neighbors   (-> (find-neighbors graph v) reverse) ;; left branch first
            not-visited (filter (complement #(visited? % visited)) neighbors)
            new-stack   (into (pop stack) not-visited)]
        (if (visited? v visited)
          (recur new-stack visited)
          (recur new-stack (conj visited v)))))))


(defn graph-bfs
  "Traverses a graph in Breadth First Search (BFS)."
  [graph v]
  (loop [queue   (conj clojure.lang.PersistentQueue/EMPTY v) ;; queue to store explored nodes
         visited []]                                         ;; A vector of visited sequence
    (if (empty? queue) visited                               ;; Base case - return visited nodes if the queue is empty
        (let [v           (peek queue)
              neighbors   (find-neighbors v graph)
              not-visited (filter (complement #(visited? % visited)) neighbors)
              new-queue   (apply conj (pop queue) not-visited)]
          (if (visited? v visited)
            (recur new-queue visited)
            (recur new-queue (conj visited v)))))))


;; Example usage: (graph-dfs graph :A)
;; Example usage: (graph-dfs graph :B)
;; Example usage: (graph-bfs graph :A)

;; ====================================================================
