function add(left, right) {
    return (left + right);
}
function subtract(left, right) {
    return left - right
}
function multiply(left, right) {
    return left * right;
}
function divide(left, right) {
    return left / right;
}
function negate(left) {
    return -left;
}
function sin(left) {
    return Math.sin(left);
}
function cos(left) {
    return (Math.cos(left));
}
function exp(left) {
    return (Math.exp(left));
}
function arcTan(left) {
    return (Math.atan(left));
}
function addDiff(f, name) {
    return (new Add(f.args[0].diff(name), f.args[1].diff(name)));
}
function subDiff(f, name) {
    return (new Subtract(f.args[0].diff(name), f.args[1].diff(name)));
}
function mulDiff(f, name) {
    return (new Add(new Multiply(f.args[0], f.args[1].diff(name)), new Multiply(f.args[0].diff(name), f.args[1])));
}
function divDiff(f, name) {
    return (new Divide(new Subtract(new Multiply(f.args[0].diff(name), f.args[1]),
        new Multiply(f.args[0], f.args[1].diff(name))),
        new Multiply(f.args[1], f.args[1])));
}
function negDiff(f, name) {
    return (new Negate(f.args[0].diff(name)));
}
function sinDiff(f, name) {
    return (new Multiply(new Cos(f.args[0]), f.args[0].diff(name)));
}
function cosDiff(f, name) {
    return (new Multiply(new Negate(new Sin(f.args[0])), f.args[0].diff(name)));
}
function expDiff(f, name) {
    return (new Exp(f.args[0]));
}
function arcTanDiff(f, name) {
    return (new Multiply(new Divide(new Const(1), new Add(new Const(1), new Multiply(f.args[0], f.args[0])))), f.args[0].diff(name));
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
        return (new Const(0));
    },
    prefix: function () {
        return this.value.toString();
    }
}
var vars = {
    'x': 0,
    'y': 1,
    'z': 2
}
function Variable(value) {
    this.number = value;
}
Variable.prototype = {
    toString: function () {
        return this.number;
    },
    evaluate: function () {
        return arguments[vars[this.number]];
    },
    diff: function (value) {
        if (value == this.number) {
            return (new Const(1));
        } else return (new Const(0));
    },
    prefix: function () {
        return this.number;
    }
}
var operations = [];

function toArray(obj) {
    var array = [];
    array.push.apply(array, obj);
    return (array);
}
function buildDoubleExpression(operation, f, diff, count) {

    function Cons() {
        this.args = toArray(arguments);
    }

    Cons.prototype = {
        toString: function () {
            var string = this.args.map(getString).join(' ');
            return (string + ' ' + operation)
        },
        evaluate: function (args) {
            if (typeof(f) != "function") return "Fail buildDoubleExpression";
            return (f.apply(null, this.args.map(getValue(arguments))))
        },
        diff: function (name) {
            return (diff(this, name));
        },
        prefix: function () {
            var string = this.args.map(getPrefix).join(' ');
            return ('(' + operation + ' ' + string + ')');
        }
    }
    Cons.prototype.count = count;
    operations[operation] = Cons;
    return Cons
}
function getString(args) {
    return args.toString();
}
function getPrefix(args) {
    return args.prefix();
}
function getValue(args) {
    return function (f) {
        return f.evaluate.apply(f, args);
    }
}
var Add = buildDoubleExpression('+', add, addDiff, 2);
var Subtract = buildDoubleExpression('-', subtract, subDiff, 2);
var Multiply = buildDoubleExpression('*', multiply, mulDiff, 2);
var Divide = buildDoubleExpression('/', divide, divDiff, 2);
var Negate = buildDoubleExpression('negate', negate, negDiff, 1);
var Sin = buildDoubleExpression('sin', sin, sinDiff, 1);
var Cos = buildDoubleExpression('cos', cos, cosDiff, 1);
var Exp = buildDoubleExpression('exp', exp, expDiff, 1);
var ArcTan = buildDoubleExpression('cos', arcTan, arcTanDiff, 1);

function ParsePrefixError(message) {
    this.name = "ParsePrefixError";
    this.message = message;
}
ParsePrefixError.prototype = Error.prototype;

function parsePrefix(expr) {
    var i = 0, lexems = [], lexem;
    while ((lexem = getPrefix()) != undefined) {
        lexems.push(lexem);
    }
    if (!lexems.length) throw new ParsePrefixError("Empty expression");
    var closeBrackets = 0;
    var exp = [];
    build();

    function getPrefix() {
        while (expr[i] == ' ') i++;
        if (i == expr.length) return (undefined)
        var j = i;
        while (expr[i] != '(' && expr[i] != ')' && expr[i] != ' ' && i < expr.length) {
            i++;
        }
        if (i == j) {
            i++;
            return (expr.substr(j, i - j));
        }
        return (expr.substr(j, i - j));
    }

    function getInt(str) {
        if (str[0] == "+") {
            throw new ParsePrefixError("Invalid symbol at " + str + "'");
        }
        var toInt = +str;
        if (parseInt(str, 10).toString() != str) {
            throw new ParsePrefixError("Invalid symbol at '" + str + "'");
        }
        return (toInt);
    }


    function build() {
        while (lexems.length > 0) {
            var op = lexems.pop();
            if (op == '(') {
                if (closeBrackets > 0) {
                    closeBrackets--;
                    build();
                } else throw new ParsePrefixError("Missing brackets")
                continue;
            }
            if (op == ')') {
                closeBrackets++;
                continue;
            }
            if (operations[op] != undefined) {
                var args = [];
                for (var i = 0; i < operations[op].prototype.count; i++) {
                    var toPush = exp.pop();
                    if (toPush == undefined) throw new ParsePrefixError("Not enough arguments for " + op);
                    args.push(toPush);
                }
                var t = Object.create(operations[op].prototype);
                operations[op].apply(t, args);
                exp.push(t);
                continue;
            }
            if (vars[op] != undefined) {
                exp.push(new Variable(op));
                continue;
            }
            exp.push(new Const(getInt(op)));
        }
    }

    if (closeBrackets != 0) throw new ParsePrefixError('Too much brackets');
    if (exp.length > 1) throw new ParsePrefixError('Too much arguments');
    return (exp.pop());
}

//expr = new Cos(new Multiply(new Sin(new Multiply(new Multiply(new Subtract(new Add(new Multiply(new Const(543234497),new Variable('x')),new Multiply(new Const(-1780810098),new Variable('y'))),new Divide(new Divide(new Variable('x'),new Divide(new Variable('y'),new Variable('y'))),new Divide(new Const(-196005095),new Variable('y')))),new Const(490877817)),new Cos(new Variable('x')))),new Divide(new Variable('y'),new Subtract(new Variable('y'),new Variable('x')))))
//println(expr.evaluate(0.49414677875518040000, 0.92395530552400210000, 0.73328612551950370000));
var exp = new Const(4);
console.log(exp);
//println(exp.prefix());