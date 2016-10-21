/**
 * Created by aydar on 17.04.16.
 */
function binaryFunc(f) {
    return function (left, right) {
        return function (x, y, z) {
            return f(left(x, y, z), right(x, y, z));
        }
    }
}

function unaryFunc(f) {
    return function (left) {
        return function (x, y, z) {
            return f(left(x, y, z));
        }
    }
}

function cnst(val) {
    return function () {
        return val;
    }
}

function variable(name) {
    return function (x, y, z) {
        switch (name) {
            case 'x':
                return x;
            case 'y':
                return y;
            case 'z':
                return z;
        }
    }
}

add = binaryFunc(function (l, r) {
    return l + r;
});
subtract = binaryFunc(function (l, r) {
    return l - r;
});
multiply = binaryFunc(function (l, r) {
    return l * r;
});
power = binaryFunc(function (l, r) {
    return Math.pow(l, r);
});
divide = binaryFunc(function (l, r) {
    return l / r;
});
mod = binaryFunc(function (l, r) {
    return l % r;
});
log = unaryFunc(function (l) {
    return Math.log(l);
});
abs = unaryFunc(function (l) {
    return Math.abs(l);
});
negate = unaryFunc(function (l) {
    return -l;
});

function parse(expression) {
    var exp = expression.split(' ').filter(function (s) {
        return s != '';
    });
    var st = [];

    function applyUnOp(st, it) {
        var left = st.pop();
        st.push(it(left));
    }

    function applyBinOp(st, it) {
        var right = st.pop();
        var left = st.pop();
        st.push(it(left, right));
    }

    exp.forEach(function (it) {
        switch (it) {
            case '+':
                applyBinOp(st, add);
                break;
            case '-':
                applyBinOp(st, subtract);
                break;
            case '*':
                applyBinOp(st, multiply);
                break;
            case '/':
                applyBinOp(st, divide);
                break;
            case '%':
                applyBinOp(st, mod);
                break;
            case '**':
                applyBinOp(st, power);
                break;
            case 'log':
                applyUnOp(st, log);
                break;
            case 'abs':
                applyUnOp(st, abs);
                break;
            case 'negate':
                applyUnOp(st, negate);
                break;
            case 'x':
            case 'y':
            case 'z':
                st.push(variable(it));
                break;
            default:
                st.push(cnst(parseInt(it)));
        }
    });

    return st.pop();
}