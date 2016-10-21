(chapter "JavaScript-like objects")

(section "Maps as objects")
(def point {"x" 10 "y" 20})
(example 'point)
(example "Map as function" '(point "x"))

(section "Keywords")
(def point {:x 10 :y 20})
(example "Keyword" :x)
(example '(= :x (keyword "x")))
(example "Keywords map" 'point)
(example "Keywords as functions" '(:x point))

(section "Prototypes")
(def shifted-point {:dx 1 :dy 2 :y 100 :prototype point})
(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    :else (proto-get (:prototype obj) key)))
(example '(proto-get shifted-point :dx))
(example '(proto-get shifted-point :x))
(example '(proto-get shifted-point :y))

(section "Methods")
(def point
  {:x 10
   :y 20
   :getX (fn [this] (proto-get this :x))
   :getY (fn [this] (proto-get this :y))
   })
(def shifted-point
  {:dx 1
   :dy 2
   :getX (fn [this] (+ (proto-get this :x) (proto-get this :dx)))
   :getY (fn [this] (+ (proto-get this :y) (proto-get this :dy)))
   :add (fn [this a b] (+ a b))
   :prototype point
   })
(defn proto-call [obj key & args]
  (apply (proto-get obj key) (cons obj args)))
(example '(proto-call point :getX))
(example '(proto-call shifted-point :getX))
(example '(proto-call shifted-point :add 2 3))

(section "Shugaring")
(defn method [key]
  (fn [obj & args] (apply (partial proto-call obj key) args)))
(defn field [key]
  (fn [obj] (proto-get obj key)))

(def _getX (method :getX))
(def _add (method :add))
(def _x (field :x))
(def _y (field :y))
(def _dx (field :dx))
(def _dy (field :dy))

(def point
  {:x 10
   :y 20
   :getX (partial _x)
   :getY (partial _y)
   })
(def shifted-point
  {:dx 1
   :dy 2
   :getX (fn [this] (+ (_x this) (_dx this)))
   :getY (fn [this] (+ (_y this) (_dy this)))
   :add (fn [this a b] (+ a b))
   :prototype point
   })
(example '(_getX point))
(example '(_getX shifted-point))
(example '(_add shifted-point 2 3))
(example '(_x point))
(example '(_x shifted-point))

(section "Constructors")
(def PointPrototype
  {:getX (partial _x)
   :getY (partial _y)
   })
(defn constructor [ctor prototype]
  (fn [& args] (apply (partial ctor {:prototype prototype}) args)))
(defn Point [this x y]
  (assoc this
    :x x
    :y y))
(def newPoint (constructor Point PointPrototype))

(def ShiftedPointPrototype
  (assoc PointPrototype
    :getX (fn [this] (+ (_x this) (_dx this)))
    :getY (fn [this] (+ (_y this) (_dy this)))
    :add (fn [this a b] (+ a b))))
(defn ShiftedPoint [this x y dx dy]
  (assoc (Point this x y)
    :dx dx
    :dy dy
    ))
(def newShiftedPoint (constructor ShiftedPoint ShiftedPointPrototype))

(def point (newPoint 10 20))
(def shifted-point (newShiftedPoint 10 20 1 2))
(example '(_getX point))
(example '(_getX shifted-point))
(example '(_add shifted-point 2 3))
(example '(_x point))
(example '(_x shifted-point))




(chapter "Java-like objects")

(section "Interfaces and Classes")

(definterface PtI
  (getX [])
  (getY []))

(deftype Pt [x y]
  PtI
  (getX [this] x)
  (getY [this] y))

(deftype ShiftedPt [x y dx dy]
  PtI
  (getX [this] (+ (.x this) (.dx this)))
  (getY [this] (+ (.y this) (.dy this))))

(def point (Pt. 10 20))
(def shifted-point (ShiftedPt. 10 20 1 2))
(example 'point)
(example '(.x point))
(example '(.y point))
(example '(.getX point))
(example '(.getY point))
(example 'shifted-point)
(example '(.getX shifted-point))
(example '(.getY shifted-point))

(section "Java method implementation")
(deftype Pair [f s]
  Object
  (equals [this that]
    (and (= f (.f that)) (= s (.s that))))
  Comparable
  (compareTo [this that]
    (cond
      (< f (.f that)) -1
      (> f (.f that)) 1
      (< s (.s that)) -1
      (> s (.s that)) 1
      :else 0)))

(example '(= (Pair. 1 2) (Pair. 1 2)))
(example '(= (Pt. 1 2) (Pt. 1 2)))
(example '(.compareTo (Pair. 1 2) (Pair. 1 1)))

(section "Mutable fields")
(definterface MPair
  (getFirst [])
  (getSecond [])
  (setFirst [value])
  (setSecond [value]))
(deftype MutablePair [^{:unsynchronized-mutable true} f
                      ^{:unsynchronized-mutable true} s]
  MPair
  (getFirst [this] f)
  (getSecond [this] s)
  (setFirst [this value] (set! f value))
  (setSecond [this value] (set! s value)))

(def mutable-pair (MutablePair. 1 2))
(defn setPair [pair f s]
  (.setFirst pair f)
  (.setSecond pair s))
(example '(.getFirst mutable-pair))
(example '(.getSecond mutable-pair))
(example '(.setFirst mutable-pair 10))
(example '(.setSecond mutable-pair 20))
(example '(.getFirst mutable-pair))
(example '(.getSecond mutable-pair))
(example '(setPair mutable-pair 100 200))
(example '(.getFirst mutable-pair))
(example '(.getSecond mutable-pair))



(chapter "Evaluation Order")

(defn indented [& xs]
  (apply println (cons "       " xs)))

(defn list-concat [& xss]
  (apply list (apply concat xss)))

(defn trace [x]
  (indented "trace" x)
  x)

(section "Applicative evaluation order")
(defn add-app [a b]
  (indented "evaluate f")
  (+ a b))

(example '(add-app (trace 1) (trace 2)))
(example '(let [v (trace 2)] (add-app v v)))

(section "Normal evaluation order")
(defn add-norm [x y]
  (indented "evaluate f")
  (+ (eval x) (eval y)))
(example '(add-norm '(trace 1) '(trace 2)))
(example '(let [v '(trace 2)] (add-norm v v)))

(section "Lazy evaluation order")
(defn add-lazy [x y]
  (indented "evaluate f")
  (+ (force x) (force y)))
(example '(add-lazy (delay (trace 1)) (delay (trace 2))))
(example '(let [v (delay (trace 2))] (add-lazy v v)))

(section "Streams")
(defn sCons [head tail] [head tail])
(defn sFirst [[head tail]] head)
(defn sRest [[head tail]] (force tail))
(def sNil nil)
(def sEmpty? (partial = sNil))
(defn sTake [n stream]
  (if (pos? n)
    (sCons (sFirst stream) (delay (sTake (- n 1) (sRest stream))))))
(defn sToList [stream]
  (if (sEmpty? stream)
    '()
    (cons (sFirst stream) (sToList (sRest stream)))))
(defn sMap [f stream]
  (sCons (f (sFirst stream)) (delay (sMap f (sRest stream)))))
(defn sFilter [f stream]
  (cond
    (sEmpty? stream) sNil
    (f (first stream)) (sCons (first stream) (delay (sFilter f (sRest stream))))
    :else (sFilter f (sRest stream))))
(defn sTakeWhile [p? stream]
  (cond
    (sEmpty? stream) sNil
    (p? (sFirst stream)) (sCons (sFirst stream) (delay (sTakeWhile p? (sRest stream))))
    :else sNil))
(defn sAny? [p? stream]
  (cond
    (sEmpty? stream) false
    (p? (sFirst stream)) true
    :else (sAny? p? (sRest stream))))

(println "Finite streams")
(example '(sEmpty? sNil))
(example '(sEmpty? (sCons 1 nil)))
(example '(sCons 1 (sCons 2 (sCons 3 nil))))
(example '(sTake 2 (sCons 1 (sCons 2 (sCons 3 nil)))))
(example '(sToList (sTake 2 (sCons 1 (sCons 2 (sCons 3 nil))))))
(example '(sToList (sFilter odd? (sCons 1 (sCons 2 (sCons 3 nil))))))
(example '(sToList (sTakeWhile (partial >= 2) (sCons 1 (sCons 2 (sCons 3 nil))))))
(example '(sAny? (partial = 2) (sCons 1 (sCons 2 (sCons 3 nil)))))
(example '(sAny? (partial = 4) (sCons 1 (sCons 2 (sCons 3 nil)))))

(println "Infinite streams")
(def stream-ones (sCons 1 (delay stream-ones)))
(defn stream-integers [i] (sCons i (delay (stream-integers (+ i 1)))))
(def primes
  (letfn [(prime? [n] (not (sAny? (fn [d] (= 0 (mod n d))) (sTakeWhile (fn [d] (>= n (* d d))) (prs)))))
          (prs [] (sCons 2 (delay (sFilter prime? (stream-integers 3)))))]
    (prs)))

(example '(sToList (sTake 10 stream-ones)))
(example '(sToList (sTake 10 stream-integers)))
(example '(sToList (sTake 10 (sMap (partial * 10) (stream-integers 0)))))
(example '(sToList (sTake 10 primes)))

(section "Lazy sequences")
(defn lazy-integers [i] (cons i (lazy-seq (lazy-integers (+ i 1)))))
(def lazy-primes
  (letfn [(prime? [n] (not-any? (fn [d] (= 0 (mod n d))) (take-while (fn [d] (>= n (* d d))) (prs))))
          (prs [] (cons 2 (filter prime? (lazy-integers 3))))]
    (prs)))
(example '(apply list (take 10 (letfn [(lazy-ones [] (cons 1 (lazy-seq (lazy-ones))))] (lazy-ones)))))
(example '(apply list (take 10 (lazy-integers 0))))
(example '(apply list (take 10 lazy-primes)))




(chapter "Evaluation Order")

(defn indented [& xs]
  (apply println (cons "       " xs)))

(defn list-concat [& xss]
  (apply list (apply concat xss)))

(defn trace [x]
  (indented "trace" x)
  x)

(section "Applicative evaluation order")
(defn add-app [a b]
  (indented "evaluate f")
  (+ a b))

(example '(add-app (trace 1) (trace 2)))
(example '(let [v (trace 2)] (add-app v v)))

(section "Normal evaluation order")
(defn add-norm [x y]
  (indented "evaluate f")
  (+ (eval x) (eval y)))
(example '(add-norm '(trace 1) '(trace 2)))
(example '(let [v '(trace 2)] (add-norm v v)))

(section "Lazy evaluation order")
(defn add-lazy [x y]
  (indented "evaluate f")
  (+ (force x) (force y)))
(example '(add-lazy (delay (trace 1)) (delay (trace 2))))
(example '(let [v (delay (trace 2))] (add-lazy v v)))

(section "Streams")
(defn sCons [head tail] [head tail])
(defn sFirst [[head tail]] head)
(defn sRest [[head tail]] (force tail))
(def sNil nil)
(def sEmpty? (partial = sNil))
(defn sTake [n stream]
  (if (pos? n)
    (sCons (sFirst stream) (delay (sTake (- n 1) (sRest stream))))))
(defn sToList [stream]
  (if (sEmpty? stream)
    '()
    (cons (sFirst stream) (sToList (sRest stream)))))
(defn sMap [f stream]
  (sCons (f (sFirst stream)) (delay (sMap f (sRest stream)))))
(defn sFilter [f stream]
  (cond
    (sEmpty? stream) sNil
    (f (first stream)) (sCons (first stream) (delay (sFilter f (sRest stream))))
    :else (sFilter f (sRest stream))))
(defn sTakeWhile [p? stream]
  (cond
    (sEmpty? stream) sNil
    (p? (sFirst stream)) (sCons (sFirst stream) (delay (sTakeWhile p? (sRest stream))))
    :else sNil))
(defn sAny? [p? stream]
  (cond
    (sEmpty? stream) false
    (p? (sFirst stream)) true
    :else (sAny? p? (sRest stream))))

(println "Finite streams")
(example '(sEmpty? sNil))
(example '(sEmpty? (sCons 1 nil)))
(example '(sCons 1 (sCons 2 (sCons 3 nil))))
(example '(sTake 2 (sCons 1 (sCons 2 (sCons 3 nil)))))
(example '(sToList (sTake 2 (sCons 1 (sCons 2 (sCons 3 nil))))))
(example '(sToList (sFilter odd? (sCons 1 (sCons 2 (sCons 3 nil))))))
(example '(sToList (sTakeWhile (partial >= 2) (sCons 1 (sCons 2 (sCons 3 nil))))))
(example '(sAny? (partial = 2) (sCons 1 (sCons 2 (sCons 3 nil)))))
(example '(sAny? (partial = 4) (sCons 1 (sCons 2 (sCons 3 nil)))))

(println "Infinite streams")
(def stream-ones (sCons 1 (delay stream-ones)))
(defn stream-integers [i] (sCons i (delay (stream-integers (+ i 1)))))
(def primes
  (letfn [(prime? [n] (not (sAny? (fn [d] (= 0 (mod n d))) (sTakeWhile (fn [d] (>= n (* d d))) (prs)))))
          (prs [] (sCons 2 (delay (sFilter prime? (stream-integers 3)))))]
    (prs)))

(example '(sToList (sTake 10 stream-ones)))
(example '(sToList (sTake 10 stream-integers)))
(example '(sToList (sTake 10 (sMap (partial * 10) (stream-integers 0)))))
(example '(sToList (sTake 10 primes)))

(section "Lazy sequences")
(defn lazy-integers [i] (cons i (lazy-seq (lazy-integers (+ i 1)))))
(def lazy-primes
  (letfn [(prime? [n] (not-any? (fn [d] (= 0 (mod n d))) (take-while (fn [d] (>= n (* d d))) (prs))))
          (prs [] (cons 2 (filter prime? (lazy-integers 3))))]
    (prs)))
(example '(apply list (take 10 (letfn [(lazy-ones [] (cons 1 (lazy-seq (lazy-ones))))] (lazy-ones)))))
(example '(apply list (take 10 (lazy-integers 0))))
(example '(apply list (take 10 lazy-primes)))



(chapter "Churh Encoding")

(section "Numbers")

(println "zero and succ")
(def zero (fn [f x] x))
(defn succ [n] (fn [f x] (f (n f x))))
(defn to-int [n] (n (partial + 1) 0))

(example '(to-int zero))
(example '(to-int (succ zero)))
(example '(to-int (succ (succ zero))))
(example '(to-int (succ (succ (succ zero)))))

(println "values")
(def one (succ zero))
(def two (succ one))
(def three (succ two))
(example '(to-int zero))
(example '(to-int one))
(example '(to-int two))
(example '(to-int three))

(println "add")
(defn add [n1 n2] (fn [f x] (n1 f (n2 f x))))
(example '(to-int (add zero zero)))
(example '(to-int (add two three)))

(println "pred")
(defn pred [n]
  (fn [f x]
    (last (n (fn [[a b]] [(f a) a]) [x x]))))
(example '(to-int (pred two)))
(example '(to-int (pred one)))
(example '(to-int (pred zero)))

(println "subtract")
(defn subtract [n1 n2] (n2 pred n1))
(example '(to-int (subtract three one)))
(example '(to-int (subtract three two)))
(example '(to-int (subtract one one)))
(example '(to-int (subtract one three)))

(section "Pairs")
(defn pair [f s] (fn [p] (p f s)))
(defn fst [p] (p (fn [f s] f)))
(defn snd [p] (p (fn [f s] s)))

(example '(def pp (pair 10 20)))
(example '(fst pp))
(example '(snd pp))

(section "Booleans")
(println "values")
(defn b-true [f s] f)
(defn b-false [f s] s)
(defn to-boolean [b] (b true false))
(example '(to-boolean b-true))
(example '(to-boolean b-false))

(println "not")
(defn b-not [b] (fn [f s] (b s f)))
(example '(to-boolean (b-not b-true)))
(example '(to-boolean (b-not b-false)))

(println "and")
(defn b-and [b1 b2] (fn [f s] (b1 (b2 f s) s)))
(example '(to-boolean (b-and b-false b-false)))
(example '(to-boolean (b-and b-true b-false)))
(example '(to-boolean (b-and b-false b-true)))
(example '(to-boolean (b-and b-true b-true)))

(println "or")
(defn b-or [b1 b2] (fn [f s] (b1 f (b2 f s))))
(example '(to-boolean (b-or b-false b-false)))
(example '(to-boolean (b-or b-true b-false)))
(example '(to-boolean (b-or b-false b-true)))
(example '(to-boolean (b-or b-true b-true)))

(println "predicates")
(defn is-zero? [n] (n (fn [x] b-false) b-true))
(defn less-or-equal? [n1 n2] (is-zero? (subtract n1 n2)))
(defn equal? [n1 n2] (b-and (less-or-equal? n1 n2) (less-or-equal? n2 n1)))

(example '(to-boolean (is-zero? zero)))
(example '(to-boolean (is-zero? three)))
(example '(to-boolean (less-or-equal? one three)))
(example '(to-boolean (less-or-equal? one one)))
(example '(to-boolean (less-or-equal? three one)))
(example '(to-boolean (equal? one three)))
(example '(to-boolean (equal? one one)))
(example '(to-boolean (equal? three one)))

(section "Signed numbers")
(defn signed [n] (pair n zero))
(defn negate [n] (pair (snd n) (fst n)))
(defn signed-add [n1 n2] (pair (add (fst n1) (fst n2)) (add (snd n1) (snd n2))))
(defn signed-subtract [n1 n2] (signed-add n1 (negate n2)))
(defn signed-to-int [n] ((fst n) (partial + 1) ((snd n) (fn [x] (- x 1)) 0)))

(example '(signed-to-int (signed zero)))
(example '(signed-to-int (signed two)))
(example '(signed-to-int (negate (signed zero))))
(example '(signed-to-int (negate (signed two))))
(example '(signed-to-int (signed-add (signed one) (signed two))))
(example '(signed-to-int (signed-subtract (signed one) (signed two))))
