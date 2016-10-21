(defn constant [v] (fn [args] v))

(defn variable [name] (fn [args] (args name)))

(defn operation [f]
      (fn [& exps]
          (fn [args]
              (apply f (mapv (fn [e] (e args)) exps)))))

(defn unary [f]
      (fn [exp]
          (fn [args]
              (f (exp args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(defn divide [a b]
      (fn [args] (/ (double (a args)) (double (b args)))))

(defn sin' [x] (Math/sin x))
(defn cos' [x] (Math/cos x))

(def sin (operation sin'))
(def cos (operation cos'))
(def negate (operation -))

(defn parser [operations c v]
      (defn parseIm [it]
            (cond
              (number? it) (c it)
              (symbol? it) (v (str it))
              (list? it) (apply
                           (operations (first it))
                           (mapv parseIm (rest it))))))

(def operationsF {'+ add '- subtract '* multiply '/ divide 'sin sin 'cos cos 'negate negate})
(defn parseFunction [s] ((parser operationsF constant variable) (read-string s)))

;(def c clojure.string/join)
;(def expr (parseFunction "(/ (negate x), 2.0)"))

;(defn p [rs] (cond
;               (symbol? rs) (print (str rs))
;               (list? rs) (print (c ["{" ]))))
;(println (clojure.string/join ", " (read-string "(/ (negate x), 2.0)")))