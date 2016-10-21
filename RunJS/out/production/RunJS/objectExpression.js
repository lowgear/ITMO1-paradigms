var ops = [];
function expression(f, sign, d, argsNum) {
    function ExpConstructor() {
        this.args = [];
        for (var i = 0; i < arguments.length; i++)
            this.args.push(arguments[i]);
    }

    ExpConstructor.prototype = {
        d: d,
        sign: sign,
        f: f,
        argsNum: argsNum,

        evaluate: function (x, y, z) {
            var applied = [];
            this.args.forEach(function (it, n) {
                applied[n] = it.evaluate(x, y, z);
            });
            return this.f.apply(this, applied);
        },

        toString: function () {
            var res = '';
            this.args.forEach(function (it) {
                res += it.toString() + ' ';
            });
            return res + this.sign;
        },

        prefix: function () {
            var res = '';
            this.args.forEach(function (it) {
                res += ' ' + it.prefix();
            });
            return '(' + this.sign + res + ')';
        },

        postfix: function () {
            var res = '';
            this.args.forEach(function (it) {
                res += it.postfix() + ' ';
            });
            return '(' + res + this.sign + ')';
        },

        diff: function (name) {
            return this.d.apply(this, [name].concat(this.args));
        }
    };

    ops[sign] = ExpConstructor;

    return ExpConstructor;
}

function Const(value) {
    this.value = value;
}
Const.prototype = {
    toString: function () {
        return (this.value.toString());
    },
    evaluate: function () {
        return (this.value);
    },
    diff: function () {
        return zeroConst;
    },
    prefix: function () {
        return this.value.toString();
    },
    postfix: function () {
        return this.value.toString();
    }
};
var zeroConst = new Const(0);
var unConst = new Const(1);

var Variable = function (name) {
    this.name = name;
};
Variable.prototype = {
    evaluate: function (x, y, z) {
        switch (this.name) {
            case 'x':
                return x;
            case 'y':
                return y;
            case 'z':
                return z;
        }
    },
    toString: function () {
        return this.name;
    },
    prefix: function () {
        return this.name;
    },
    postfix: function () {
        return this.name;
    },
    diff: function (_name) {
        if (this.name === _name)
            return unConst;
        return zeroConst;
    }
};

var AddDiff = function (name, left, right) {
    return new Add(left.diff(name), right.diff(name));
};
var Add = expression(function (left, right) {
    return left + right;
}, '+', AddDiff, 2);

var SubtractDiff = function (name, left, right) {
    return new Subtract(left.diff(name), right.diff(name));
};
var Subtract = expression(function (left, right) {
    return left - right;
}, '-', SubtractDiff, 2);

var MultiplyDiff = function (name, left, right) {
    return new Add(
        new Multiply(left.diff(name), right),
        new Multiply(left, right.diff(name))
    );
};
var Multiply = expression(function (left, right) {
    return left * right;
}, '*', MultiplyDiff, 2);

/*var Power = binaryExp(function (left, right) {
 return Math.pow(left, right);
 }, '**');*/

var DivideDiff = function (name, left, right) {
    return new Divide(
        new Subtract(
            new Multiply(left.diff(name), right),
            new Multiply(left, right.diff(name))
        ),
        new Multiply(right, right)
    );
};
var Divide = expression(function (left, right) {
    return left / right;
}, '/', DivideDiff, 2);

/*var Mod = binaryExp(function (left, right) {
 return left % right;
 }, '%');*/

var LogDiff = function (name, left) {
    return new Divide(left.diff(name), left);
};
var Log = expression(function (left) {
    return Math.log(left);
}, 'log', LogDiff, 1);

/*var Abs = unaryExp(function (left) {
 return Math.abs(left);
 }, 'abs');*/

var NegateDiff = function (name, left) {
    return new Negate(left.diff(name));
};
var Negate = expression(function (left) {
    return -left;
}, 'negate', NegateDiff, 1);

var SinDiff = function (name, left) {
    return new Multiply(new Cos(left), left.diff(name));
};
var Sin = expression(function (left) {
    return Math.sin(left);
}, 'sin', SinDiff, 1);

var CosDiff = function (name, left) {
    return new Negate(new Multiply(new Sin(left), left.diff(name)));
};
var Cos = expression(function (l) {
    return Math.cos(l);
}, 'cos', CosDiff, 1);

var ExpDiff = function (name, left) {
    return new Multiply(left.diff(name), new Exp(left));
};
var Exp = expression(function (l) {
    return Math.exp(l);
}, 'exp', ExpDiff, 1);

var ArcTanDiff = function (name, left) {
    var underLine = new Add(unConst, new Multiply(left, left));
    return new Divide(left.diff(name), underLine)
};
var ArcTan = expression(function (l) {
    return Math.atan(l);
}, 'atan', ArcTanDiff, 1);

var newExp = function (Exp) {
    function F(args) {
        return Exp.apply(this, args);
    }

    F.prototype = Exp.prototype;

    return function () {
        return new F(arguments);
    }
};

function parse(expression) {
    return parsePostfix(expression);
}

function exception(pos, message) {
    throw "Parsing exception: " + message + " at " + pos;
}

function tokenize(s) {
    function tokenize() {
        var st = [2, 0];
        var brackets;

        function passWhite() {
            var wsnum = 0;
            while (pos < s.length && s.charAt(pos) == ' ') {
                wsnum++;
                pos++;
            }
            st.push(wsnum);
        }

        passWhite();

        if (pos >= s.length)
            exception(pos, "symbols expected");
        if (brackets = (s.charAt(pos) == '(')) {
            st.push(1);
            pos++;
        }
        while (true) {
            if (pos >= s.length)
                if (brackets)
                    exception(pos, "closing bracket expected");
                else
                    return st;
            curSymb = s.charAt(pos)
            switch (curSymb) {
                case ' ':
                    passWhite();
                    break;
                case '(':
                    st.push(tokenize());
                    break;
                case ')':
                    if (!brackets)
                        exception(pos, "closing brackets unexpected");
                    st.push(1);
                    pos++;
                    passWhite();
                    return st;
                default:
                    var _pos = pos;
                    while (_pos < s.length && (curSymb = s.charAt(_pos)) != ' ' && curSymb != ')' && curSymb != '(')
                        _pos++;
                    st.push(s.substring(pos, _pos));
                    pos = _pos;
                    break;
            }
        }
    }

    var pos = 0;
    var res = tokenize();
    if (pos < s.length)
        exception(pos, "extra symbols found");
    return res;
}

function passWhite(st) {
    while (st[0] < st.length && typeof st[st[0]] == "number")
        st[1] += st[st[0]++];
}

function checkExtraSymbols(st) {
    if (st[0] < st.length) {
        exception(st[1], "extra symbols");
    }
}

function abstractParse(parseToken, s) {
    var st = tokenize(s);
    var res = parseToken(st);
    checkExtraSymbols(st);
    return res;
}

function parsePrefix(s) {
    function parseToken(st) {
        function take(arg) {
            st[1] += st[st[0]++].length;
            var res = arg;
            passWhite(st);
            return res;
        }

        function takeOp(arg) {
            pos += st[st[0]++].length;
            var res = arg();
            passWhite(st);
            return res;
        }

        function applyOp(it) {
            st[1] += st[st[0]++].length;
            var args = [];
            for (var i = 0; i < it.prototype.argsNum; i++)
                args.push(parseToken(st));
            var res = newExp(it).apply(this, args);
            passWhite(st);
            return res;
        }

        passWhite(st);
        if (st[0] >= st.length)
            exception(st[1], "symbols expected");

        if (typeof st[st[0]] == "object") {
            var res = parseToken(st[st[0]]);
            checkExtraSymbols(st[st[0]]);
            st[1] += st[st[0]][1];
            passWhite(st[st[0]]);
            st[0]++;
            return res;
        }

        if (ops[st[st[0]]] != undefined)
            return applyOp(ops[st[st[0]]]);
        else
            switch (st[st[0]]) {
                case 'x':
                case 'y':
                case 'z':
                    return take(new Variable(st[st[0]]));
                default:
                    if (isNaN(st[st[0]]))
                        exception(st[1], "illegal word");
                    return take(new Const(parseFloat(st[st[0]])));
            }
    }

    return abstractParse(parseToken, s);
}

function parsePostfix(s) {
    function parseToken(st) {
        var q = [];

        function take(arg) {
            st[1] += st[st[0]++].length;
            passWhite(st);
            q.push(arg);
        }

        function applyOp(Op) {
            var args = [];
            for (var i = 0; i < Op.prototype.argsNum; i++)
                if (q.length > 0)
                    args.push(q.pop());
                else
                    exception(st[1], "arguments expected by operator");
            st[1] += st[st[0]++].length;
            args = args.reverse();
            var res = newExp(Op).apply(this, args);
            passWhite(st);
            q.push(res);
        }

        passWhite(st);
        if (st[0] >= st.length)
            exception(st[1], "symbols expected");

        while (st[0] < st.length) {
            if (typeof st[st[0]] == "object") {
                q.push(parseToken(st[st[0]]));
                checkExtraSymbols(st[st[0]]);
                st[1] += st[st[0]][1];
                passWhite(st[st[0]]);
                st[0]++;
            } else
            if (ops[st[st[0]]] != undefined)
                applyOp(ops[st[st[0]]]);
            else
                switch (st[st[0]]) {
                    case 'x':
                    case 'y':
                    case 'z':
                        take(new Variable(st[st[0]]));
                        break;
                    default:
                        if (isNaN(st[st[0]]))
                            exception(st[1], "illegal word");
                        take(new Const(parseFloat(st[st[0]])));
                }
        }

        if (q.length > 1)
            exception(st[1], "extra symbols found");
        return q.pop();
    }

    return abstractParse(parseToken, s);
}

//in expr.evaluate(0.49414677875518040000, 0.92395530552400210000, 0.73328612551950370000);
//where expr = new Cos(new Multiply(new Sin(new Multiply(new Multiply(new Subtract(new Add(new Multiply(new Const(543234497),new Variable('x')),new Multiply(new Const(-1780810098),new Variable('y'))),new Divide(new Divide(new Variable('x'),new Divide(new Variable('y'),new Variable('y'))),new Divide(new Const(-196005095),new Variable('y')))),new Const(490877817)),new Cos(new Variable('x')))),new Divide(new Variable('y'),new Subtract(new Variable('y'),new Variable('x')))))
//: Expected -0.487615029700, found 0.497328934829
//expr = Math.cos((Math.sin((((((543234497 * x) + (-1780810098 * y)) - ((x / (y / y)) / (-196005095 / y))) * 490877817) * Math.cos(x))) * (y / (y - x))))
//println(expr);

// var expr = parse('4 x *').diff('z');
// console.log(expr.postfix());
// expr = expr.simplify();
// var s = expr.postfix();
// console.log(expr.postfix());