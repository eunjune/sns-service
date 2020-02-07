(window.webpackJsonp = window.webpackJsonp || []).push([[0], {
    39: function (e, t, n) {
        e.exports = n(93)
    }, 45: function (e, t, n) {
    }, 46: function (e, t, n) {
    }, 54: function (e, t, n) {
    }, 83: function (e, t, n) {
    }, 84: function (e, t, n) {
    }, 86: function (e, t, n) {
    }, 87: function (e, t, n) {
    }, 90: function (e, t, n) {
    }, 91: function (e, t, n) {
    }, 93: function (e, t, n) {
        "use strict";
        n.r(t);
        var a = n(0), r = n.n(a), c = n(15), o = n.n(c), s = (n(44), n(45), n(46), n(5)), l = n(94), i = n(95),
            u = n(96), m = n(97), f = n(98), p = n(99), d = n(100), h = n(10), E = n(9), g = (n(54), n(20)), b = n(18),
            v = n(6), w = n(21), O = n.n(w), j = function () {
                return localStorage.getItem("auth_token")
            }, N = {
                postUesrId: null, auth: function () {
                    var e = localStorage.getItem("auth_token");
                    return e ? O()(e) : null
                }(), posts: [], friends: [], query: {}, comments: {}, postModalOpened: !1
            }, y = Object(a.createContext)(), k = function (e, t) {
                switch (t.type) {
                    case"login":
                        return Object(v.a)({}, e, {auth: t.auth});
                    case"logout":
                        return Object(v.a)({}, N, {auth: null});
                    case"newPostCreated":
                        return Object(v.a)({}, e, {posts: [t.post].concat(Object(b.a)(e.posts))});
                    case"appendPosts":
                        return Object(v.a)({}, e, {
                            posts: t.query.offset != e.query.offset ? [].concat(Object(b.a)(e.posts), Object(b.a)(t.posts)) : e.posts,
                            query: t.query,
                            postUesrId: t.postUesrId
                        });
                    case"getPosts":
                        return Object(v.a)({}, e, {posts: t.posts, query: t.query, postUesrId: t.postUesrId});
                    case"getComments":
                        return Object(v.a)({}, e, {comments: Object(v.a)({}, e.comments, Object(g.a)({}, t.postId, t.comments))});
                    case"getFriedns":
                        return Object(v.a)({}, e, {friends: t.friends});
                    case"updatePost":
                        return Object(v.a)({}, e, {
                            posts: e.posts.map(function (e) {
                                return e.seq === t.post.seq ? t.post : e
                            })
                        });
                    case"openPostModal":
                        return Object(v.a)({}, e, {postModalOpened: !0});
                    case"togglePostModal":
                        return Object(v.a)({}, e, {postModalOpened: t.postModalOpened});
                    default:
                        return e
                }
            }, C = function (e) {
                var t = e.reducer, n = e.initialState, c = e.children;
                return r.a.createElement(y.Provider, {value: Object(a.useReducer)(t, n)}, c)
            }, I = function () {
                return Object(a.useContext)(y)
            }, x = n(11), S = n.n(x), q = "/api";

        function P(e) {
            var t = e.email, n = e.passwd;
            return S.a.post("".concat(q, "/auth"), {principal: t, credentials: n}).then(function (e) {
                return e.data
            }).then(function (e) {
                if (!e.success) throw new Error;
                return e.response
            })
        }

        function U(e, t) {
            return S.a.get("".concat(q, "/user/").concat(e, "/post/").concat(t, "/comment/list"), Object(v.a)({}, M())).then(function (e) {
                return e.data
            }).then(function (e) {
                if (!e.success) throw new Error(com.github.prgrms.social.api.error.message);
                return e.response
            })
        }

        function M() {
            return j() ? {headers: {api_key: "Bearer " + j()}} : {}
        }

        S.a.defaults.headers.post["Content-Type"] = "application/json";
        var K = function () {
            var e = Object(a.useState)(!1), t = Object(s.a)(e, 2), n = t[0], c = t[1], o = I(), g = Object(s.a)(o, 2),
                b = g[0].auth, v = g[1], w = Object(a.useState)(), O = Object(s.a)(w, 2), j = O[0], N = O[1];
            return Object(a.useEffect)(function () {
                b && S.a.get("".concat(q, "/user/me"), M()).then(function (e) {
                    return e.data
                }).then(function (e) {
                    if (!e.success) throw new Error;
                    return e.response
                }).then(function (e) {
                    return N(e.profileImageUrl)
                }).catch(alert)
            }, []), r.a.createElement(l.a, {
                fixed: "true",
                color: "blue",
                dark: !0,
                expand: "md",
                sticky: "top"
            }, r.a.createElement(E.a, {to: "/"}, r.a.createElement(i.a, {
                tag: "span",
                className: "mr-auto"
            }, r.a.createElement("img", {
                src: "/img/bi-symbol-light.png",
                alt: "\ud504\ub85c\uadf8\ub798\uba38\uc2a4 \ub85c\uace0"
            }))), r.a.createElement(u.a, {
                onClick: function () {
                    c(!n)
                }, className: "mr-2"
            }), r.a.createElement(m.a, {isOpen: n, navbar: !0}, r.a.createElement(f.a, {
                navbar: !0,
                className: "mr-auto"
            }, b && r.a.createElement(p.a, null, r.a.createElement(d.a, {
                onClick: function () {
                    return v({type: "openPostModal"})
                }
            }, r.a.createElement(h.g, {
                icon: h.e,
                size: "small"
            }), " \uacf5\uc720\ud558\uae30")), b && r.a.createElement(p.a, null, r.a.createElement(E.a, {to: "/friends"}, r.a.createElement(d.a, {tag: "span"}, r.a.createElement(h.g, {
                icon: h.c,
                size: "small"
            }), " \uce5c\uad6c")))), null != b ? r.a.createElement(f.a, {navbar: !0}, r.a.createElement(p.a, null, r.a.createElement(d.a, {tag: "span"}, j && r.a.createElement("img", {src: j}), b.name ? b.name : b.email)), r.a.createElement(p.a, null, r.a.createElement(d.a, {
                onClick: function () {
                    return localStorage.removeItem("auth_token"), v({type: "logout"}), !1
                }
            }, "\ub85c\uadf8\uc544\uc6c3"))) : r.a.createElement(f.a, {navbar: !0}, r.a.createElement(p.a, null, r.a.createElement(E.a, {to: "/login"}, r.a.createElement(d.a, {tag: "span"}, "\ub85c\uadf8\uc778"))), r.a.createElement(p.a, null, r.a.createElement(E.a, {to: "/signup"}, r.a.createElement(d.a, {tag: "span"}, "\ud68c\uc6d0\uac00\uc785"))))))
        }, T = (n(83), n(101)), F = (n(84), n(16)), z = n.n(F), A = function (e) {
            return r.a.createElement(r.a.Fragment, null, r.a.createElement("div", {className: "contents-accounts inline-items"}, r.a.createElement("div", {className: "contents-date"}, r.a.createElement("a", {
                className: "h6 post__author-name",
                href: "#"
            }, e.writer.name), r.a.createElement("div", {className: "published-date"}, r.a.createElement(z.a, {
                fromNow: !0,
                ago: !0
            }, e.createAt)))), r.a.createElement("p", null, e.contents))
        };

        function _(e) {
            var t = I(), n = Object(s.a)(t, 2), c = n[0], o = c.postUesrId, l = c.comments, i = n[1],
                u = Object(a.useState)(""), m = Object(s.a)(u, 2), f = m[0], p = m[1], d = function (t) {
                    i({type: "getComments", postId: e.postId, comments: t})
                };
            Object(a.useEffect)(function () {
                U(o, e.postId).then(d)
            }, []);
            var h = l[e.postId] || [];
            return r.a.createElement("ul", {className: "comments"}, h.map(function (e) {
                return r.a.createElement("li", {className: "comment", key: e.seq}, r.a.createElement(A, e))
            }), r.a.createElement("form", {className: "comment-form inline-items"}, r.a.createElement("div", {className: "contents-accounts inline-items"}, r.a.createElement("div", {className: "form-group"}, r.a.createElement("textarea", {
                value: f,
                onChange: function (e) {
                    return p(e.target.value)
                },
                className: "form-control",
                placeholder: ""
            }))), r.a.createElement(T.a, {
                color: "primary", onClick: function (t) {
                    if (0 === f.length) return !1;
                    var n, a, r;
                    (n = o, a = e.postId, r = f, S.a.post("".concat(q, "/user/").concat(n, "/post/").concat(a, "/comment"), {contents: r}, M()).then(function (e) {
                        return e.data
                    }).then(function (e) {
                        if (!e.success) throw new Error;
                        return e.response
                    })).then(function (t) {
                        return U(o, e.postId)
                    }).then(d).then(function (e) {
                        return p("")
                    }).catch(alert)
                }
            }, "\uacf5\uc720\ud558\uae30")))
        }

        function L(e) {
            return r.a.createElement("div", {className: "ui-box"}, r.a.createElement("article", {className: "contents"}, r.a.createElement("div", {className: "contents-accounts inline-items"}, r.a.createElement("div", {className: "contents-date"}, r.a.createElement("a", {className: "h6 contents-accounts-name"}, e.username), r.a.createElement("div", {className: "published-date"}, r.a.createElement(z.a, {
                fromNow: !0,
                ago: !0
            }, e.createAt))), r.a.createElement("div", {className: "more"}, r.a.createElement(h.g, {
                icon: h.b,
                size: "small"
            }), r.a.createElement("ul", {className: "featured-dropdown"}, r.a.createElement("li", null, r.a.createElement("a", {href: "#"}, "\uac8c\uc2dc\ubb3c \uc218\uc815")), r.a.createElement("li", null, r.a.createElement("a", {href: "#"}, "\uac8c\uc2dc\ubb3c \uc0ad\uc81c"))))), r.a.createElement("p", null, e.contents), r.a.createElement("div", {className: "contents-info inline-items"}, r.a.createElement("a", {
                href: "#",
                onClick: e.onLikeClick
            }, r.a.createElement("div", {className: "contents-icon inline-items" + (e.likesOfMe ? " likesOfMe" : "")}, r.a.createElement(h.g, {
                icon: h.f,
                size: "small"
            }), r.a.createElement("span", null, e.likes, "\uac1c"))), r.a.createElement("a", {href: ""}, r.a.createElement("div", {className: "reply"}, r.a.createElement(h.g, {
                icon: h.a,
                size: "small"
            }), r.a.createElement("span", null, e.comments, "\uac1c"))))), r.a.createElement(_, {
                postId: e.seq,
                userId: e.userId
            }))
        }

        n(86);
        var R = function (e) {
            var t = e.friendId, n = I(), c = Object(s.a)(n, 2), o = c[0], l = o.posts, i = o.auth, u = c[1],
                m = Object(a.useState)(null), f = Object(s.a)(m, 2), p = f[0], d = f[1],
                h = Object(a.useState)({offset: 0, limit: 5}), g = Object(s.a)(h, 2), b = g[0], w = g[1],
                O = Object(a.useRef)(b), j = Object(a.useRef)(l);
            Object(a.useEffect)(function () {
                O.current = {offset: b.offset, limit: b.limit}
            }), Object(a.useEffect)(function () {
                j.current = l
            });
            var N = function (e) {
                d(null), u({
                    type: 0 === b.offset ? "getPosts" : "appendPosts",
                    posts: e,
                    query: b,
                    postUesrId: t || i.userKey
                })
            }, y = function (e) {
                console.error(e), 404 === e.response.status && d("\uc0ac\uc6a9\uc790\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.")
            }, k = function (e) {
                var t = document.documentElement,
                    n = Math.max(window.pageYOffset, t.scrollTop, document.body.scrollTop) + window.innerHeight,
                    a = t.offsetHeight;
                Math.round(n) == a && j.current.length == O.current.offset + 5 && w({
                    offset: O.current.offset + 5,
                    limit: 5
                })
            };
            Object(a.useEffect)(function () {
                return window.addEventListener("scroll", k), function () {
                    window.addEventListener("scroll", k)
                }
            }, []), Object(a.useEffect)(function () {
                (t || i) && function (e, t) {
                    var n = t.offset, a = t.limit;
                    return S.a.get("".concat(q, "/user/").concat(e, "/post/list"), Object(v.a)({}, M(), {
                        params: {
                            offset: n,
                            limit: a
                        }
                    })).then(function (e) {
                        return e.data
                    }).then(function (e) {
                        if (!e.success) throw new Error(com.github.prgrms.social.api.error.message);
                        return e.response
                    })
                }(t || i.userKey, b).then(N).catch(y)
            }, [t, b]);
            var C = function (e) {
                var n, a;
                (n = t || i.userKey, a = e.seq, S.a.patch("".concat(q, "/user/").concat(n, "/post/").concat(a, "/like"), {}, M()).then(function (e) {
                    return e.data
                }).then(function (e) {
                    if (!e.success) throw new Error;
                    return e.response
                })).then(function (t) {
                    console.log(e), u({type: "updatePost", post: t})
                })
            };
            return r.a.createElement("div", {className: "row"}, p ? r.a.createElement("div", {className: "col-12 text-center mt-5"}, r.a.createElement("h1", null, p), " ") : t ? l.map(function (e) {
                return r.a.createElement(L, Object.assign({key: e.seq}, e, {
                    username: e.writer ? e.writer.name : e.writer.email.address,
                    onLikeClick: function (t) {
                        C(e), t.preventDefault()
                    }
                }))
            }) : i ? l.map(function (e) {
                return r.a.createElement(L, Object.assign({key: e.seq}, e, {
                    username: i.name ? i.name : i.email,
                    onLikeClick: function (t) {
                        C(e), t.preventDefault()
                    }
                }))
            }) : r.a.createElement(E.b, {to: "/login", noThrow: !0}))
        }, W = (n(87), function (e) {
            return r.a.createElement("li", null, r.a.createElement("div", {className: "user-thumb"}), r.a.createElement("div", {className: "noti-text"}, r.a.createElement(E.a, {
                to: "./".concat(e.seq),
                className: "h6 noti-account"
            }, e.name ? e.name : e.email.address)), r.a.createElement("span", {className: "noti-icon"}, r.a.createElement(T.a, {color: "danger"}, r.a.createElement(h.g, {
                icon: h.d,
                size: "small"
            }), " \uce5c\uad6c \uc81c\uac70")))
        }), B = function () {
            var e = I(), t = Object(s.a)(e, 2), n = t[0].friends, c = t[1];
            return Object(a.useEffect)(function () {
                S.a.get("".concat(q, "/user/connections"), M()).then(function (e) {
                    return e.data
                }).then(function (e) {
                    if (!e.success) throw new Error(com.github.prgrms.social.api.error.message);
                    return e.response
                }).then(function (e) {
                    c({type: "getFriedns", friends: e})
                })
            }, [n.length]), r.a.createElement("div", {className: "row"}, r.a.createElement("div", {className: "ui-box"}, r.a.createElement("div", {className: "dropdown-title"}, r.a.createElement("h6", {className: "title"}, "\uce5c\uad6c\ubaa9\ub85d")), r.a.createElement("ul", {className: "noti-list friend-requests"}, n.length > 0 ? n.map(function (e) {
                return r.a.createElement(W, Object.assign({key: e.seq}, e))
            }) : r.a.createElement("li", null, "\uce5c\uad6c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4."))))
        }, D = n(25), V = n.n(D), J = n(38), Y = (n(90), function () {
            var e = r.a.useState(""), t = Object(s.a)(e, 2), n = t[0], a = t[1], c = r.a.useState(""),
                o = Object(s.a)(c, 2), l = o[0], i = o[1], u = I(), m = Object(s.a)(u, 2), f = m[0].auth, p = m[1];
            if (f) return r.a.createElement(E.b, {to: "/", noThrow: !0});
            var d = function () {
                var e = Object(J.a)(V.a.mark(function e() {
                    var t, a;
                    return V.a.wrap(function (e) {
                        for (; ;) switch (e.prev = e.next) {
                            case 0:
                                return e.prev = 0, e.next = 3, P({email: n, passwd: l});
                            case 3:
                                t = e.sent, a = O()(t.apiToken), localStorage.setItem("auth_token", t.apiToken), p({
                                    type: "login",
                                    auth: a
                                }), e.next = 12;
                                break;
                            case 9:
                                e.prev = 9, e.t0 = e.catch(0), alert(e.t0);
                            case 12:
                                return e.abrupt("return", !1);
                            case 13:
                            case"end":
                                return e.stop()
                        }
                    }, e, null, [[0, 9]])
                }));
                return function () {
                    return e.apply(this, arguments)
                }
            }();
            return r.a.createElement(r.a.Fragment, null, r.a.createElement("h1", {className: "text-center login-title"}, "\uc6f9 \ud2b8\ub799 \ub85c\uadf8\uc778"), r.a.createElement("div", {className: "account-wall"}, r.a.createElement("form", {className: "form-signin"}, r.a.createElement("input", {
                type: "email",
                value: n,
                onChange: function (e) {
                    a(e.currentTarget.value)
                },
                className: "form-control",
                placeholder: "Email",
                required: !0,
                autoFocus: !0
            }), r.a.createElement("input", {
                type: "password", value: l, onChange: function (e) {
                    i(e.currentTarget.value)
                }, className: "form-control", placeholder: "Password", required: !0
            }), r.a.createElement("button", {
                onClick: function () {
                    return d(), !1
                }, className: "btn btn-lg btn-primary btn-block", type: "button"
            }, "\ub85c\uadf8\uc778"))), r.a.createElement("p", {className: "text-help text-center"}, "\uacc4\uc815\uc774 \ud544\uc694\ud558\uc2e0\uac00\uc694?", " ", r.a.createElement(E.a, {
                to: "/signup",
                className: "text-center new-account"
            }, "\uacc4\uc815 \ub9cc\ub4e4\uae30")))
        }), H = (n(91), function () {
            var e = Object(a.useState)(""), t = Object(s.a)(e, 2), n = t[0], c = t[1], o = Object(a.useState)(""),
                l = Object(s.a)(o, 2), i = l[0], u = l[1], m = Object(a.useState)(""), f = Object(s.a)(m, 2), p = f[0],
                d = f[1], h = Object(a.useState)(""), g = Object(s.a)(h, 2), b = g[0], v = g[1],
                w = Object(a.useState)(""), O = Object(s.a)(w, 2), j = O[0], N = O[1];
            return r.a.createElement(r.a.Fragment, null, r.a.createElement("h1", {className: "text-center signup-title"}, "\uacc4\uc815 \ub9cc\ub4e4\uae30"), r.a.createElement("div", {className: "account-wall"}, r.a.createElement("form", {
                className: "form-signup", onSubmit: function (e) {
                    var t;
                    if (e.preventDefault(), p === b) return (t = n, S.a.post("".concat(q, "/user/exists"), {address: t}).then(function (e) {
                        return e.data
                    }).then(function (e) {
                        if (!e.success) throw new Error;
                        return e.response
                    })).then(function (e) {
                        if (!0 === e) throw new Error("".concat(n, " \ud574\ub2f9 \uc774\uba54\uc77c\uc740 \uc774\ubbf8 \uc874\uc7ac \ud569\ub2c8\ub2e4."));
                        return !0
                    }).then(function (e) {
                        return function (e, t, n, a) {
                            var r = new FormData;
                            return r.append("principal", e), r.append("name", t), r.append("credentials", n), r.append("file", a), S.a.post("".concat(q, "/user/join"), r, {headers: {"content-type": "multipart/form-data"}}).then(function (e) {
                                return e.data
                            }).then(function (e) {
                                if (console.log("???", e), !e.success) throw new Error;
                                return e.response
                            })
                        }(n, i, p, j)
                    }).then(function (e) {
                        console.log("!!!", e), alert("".concat(n, "\ub85c \uac00\uc785\uc774 \uc644\ub8cc\ub418\uc5c8\uc2b5\ub2c8\ub2e4.")), Object(E.d)("/login")
                    }).catch(function (e) {
                        e.response && alert(e.response.data.message), e.message && alert(e.message), console.dir(e)
                    }), !1;
                    alert("\ud328\uc2a4\uc6cc\ub4dc\uac00 \ub3d9\uc77c\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4.")
                }
            }, r.a.createElement("div", {className: "form-group"}, r.a.createElement("input", {
                type: "text",
                className: "form-input",
                name: "email",
                id: "email",
                placeholder: "Your Email",
                value: n,
                onChange: function (e) {
                    return c(e.target.value)
                }
            })), r.a.createElement("div", {className: "form-group"}, r.a.createElement("input", {
                type: "text",
                className: "form-input",
                name: "name",
                id: "name",
                placeholder: "Your Name",
                value: i,
                onChange: function (e) {
                    return u(e.target.value)
                }
            })), r.a.createElement("div", {className: "form-group"}, r.a.createElement("input", {
                type: "file",
                className: "form-input",
                name: "file",
                id: "file",
                placeholder: "\uc0ac\uc9c4",
                onChange: function (e) {
                    return N(e.target.files[0])
                }
            })), r.a.createElement("div", {className: "form-group"}, r.a.createElement("input", {
                type: "password",
                className: "form-input",
                name: "password",
                id: "password",
                placeholder: "Password",
                value: p,
                onChange: function (e) {
                    return d(e.target.value)
                }
            })), r.a.createElement("div", {className: "form-group"}, r.a.createElement("input", {
                type: "password",
                className: "form-input",
                name: "re_password",
                id: "re_password",
                placeholder: "Repeat your password",
                value: b,
                onChange: function (e) {
                    return v(e.target.value)
                }
            })), r.a.createElement("button", {
                className: "btn btn-lg btn-primary btn-block",
                type: "submit"
            }, "\uac00\uc785\ud558\uae30"))), r.a.createElement("p", {className: "text-help text-center"}, "\uc774\ubbf8 \uacc4\uc815\uc774 \uc788\uc73c\uc2e0\uac00\uc694?", " ", r.a.createElement(E.a, {
                to: "/login",
                className: "text-center login-here"
            }, "\ub85c\uadf8\uc778 \ud558\uae30")))
        }), G = n(105), Z = n(102), Q = n(103), $ = n(104);
        var X = function () {
            var e = Object(a.useState)(""), t = Object(s.a)(e, 2), n = t[0], c = t[1], o = I(), l = Object(s.a)(o, 2),
                i = l[0], u = i.postModalOpened, m = i.auth, f = l[1], p = function () {
                    f({type: "togglePostModal", postModalOpened: !u})
                };
            return r.a.createElement(G.a, {
                isOpen: u,
                toggle: p,
                className: "post-modal"
            }, r.a.createElement(Z.a, {toggle: p}, "\uac8c\uc2dc\ubb3c \uc791\uc131"), r.a.createElement(Q.a, null, r.a.createElement("form", null, r.a.createElement("textarea", {
                className: "form-control input-lg",
                autoFocus: !0,
                placeholder: "\ubb34\uc2a8 \uc0dd\uac01\uc744 \ud558\uace0 \uacc4\uc2e0\uac00\uc694?",
                spellCheck: !1,
                value: n,
                onChange: function (e) {
                    return c(e.target.value)
                }
            }))), r.a.createElement($.a, null, r.a.createElement(T.a, {
                color: "primary", onClick: function () {
                    n.length > 0 && function (e) {
                        return S.a.post("".concat(q, "/post"), {contents: e}, M()).then(function (e) {
                            return e.data
                        }).then(function (e) {
                            if (!e.success) throw new Error;
                            return e.response
                        })
                    }(n).then(function (e) {
                        try {
                            Object(E.d)("/")
                        } catch (t) {
                            console.log(t), console.dir(t)
                        }
                        return e
                    }).then(function (e) {
                        f({type: "newPostCreated", post: e, postUesrId: m.userKey}), c(""), p()
                    }).catch(function (e) {
                        e.response && alert(e.response.data.message), console.dir(e)
                    })
                }
            }, "\uacf5\uc720\ud558\uae30")))
        };
        n(92);
        z.a.globalLocale = "ko";
        var ee = function () {
                return r.a.createElement(C, {
                    initialState: N,
                    reducer: k
                }, r.a.createElement(K, null), r.a.createElement("div", {className: "container"}, r.a.createElement(E.c, null, r.a.createElement(R, {path: "/"}), r.a.createElement(B, {path: "/friends"}), r.a.createElement(R, {path: "/friends/:friendId"}), r.a.createElement(Y, {path: "/login"}), r.a.createElement(H, {path: "/signup"}))), r.a.createElement(X, null))
            },
            te = Boolean("localhost" === window.location.hostname || "[::1]" === window.location.hostname || window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));

        function ne(e, t) {
            navigator.serviceWorker.register(e).then(function (e) {
                e.onupdatefound = function () {
                    var n = e.installing;
                    null != n && (n.onstatechange = function () {
                        "installed" === n.state && (navigator.serviceWorker.controller ? (console.log("New content is available and will be used when all tabs for this page are closed. See https://bit.ly/CRA-PWA."), t && t.onUpdate && t.onUpdate(e)) : (console.log("Content is cached for offline use."), t && t.onSuccess && t.onSuccess(e)))
                    })
                }
            }).catch(function (e) {
                console.error("Error during service worker registration:", e)
            })
        }

        function ae(e) {
            var t = (e + "=".repeat((4 - e.length % 4) % 4)).replace(/\-/g, "+").replace(/_/g, "/"), n = window.atob(t);
            return Uint8Array.from(Object(b.a)(n).map(function (e) {
                return e.charCodeAt(0)
            }))
        }

        new Promise(function (e, t) {
            var n = Notification.requestPermission(function (t) {
                e(t)
            });
            n && n.then(e, t)
        }).then(function (e) {
            return "granted" !== e && console.error("We weren't granted permission."), e
        }).then(function (e) {
            "denied" === e ? console.log("[Notification.requestPermission] The user has blocked notifications.") : "granted" === e && console.log("[Notification.requestPermission] Initializing service worker.")
        }), o.a.render(r.a.createElement(ee, null), document.getElementById("root")), function (e) {
            if (console.log(e), "serviceWorker" in navigator) {
                if (new URL("", window.location.href).origin !== window.location.origin) return;
                window.addEventListener("load", function () {
                    var t = "".concat("", "/service-worker-noti.js");
                    te ? (function (e, t) {
                        fetch(e).then(function (n) {
                            var a = n.headers.get("content-type");
                            404 === n.status || null != a && -1 === a.indexOf("javascript") ? navigator.serviceWorker.ready.then(function (e) {
                                e.unregister().then(function () {
                                    window.location.reload()
                                })
                            }) : ne(e, t)
                        }).catch(function () {
                            console.log("No internet connection found. App is running in offline mode.")
                        })
                    }(t, e), navigator.serviceWorker.ready.then(function (e) {
                        var t = {
                            userVisibleOnly: !0,
                            applicationServerKey: ae("BFJsnkDcKmsymlt5ZVUlIrGtqUxKVEiZ0iBRV3NKCFHrizInoT9FuA8MYESQVzbrTx4njj7lC3Iiz7K41dKUwGE")
                        };
                        return e.pushManager.subscribe(t)
                    }).then(function (e) {
                        var t = e.endpoint, n = e.getKey("p256dh"), a = e.getKey("auth");
                        return console.log("Received PushSubscription: ", JSON.stringify(e)), function (e) {
                            return S.a.post("".concat(q, "/subscribe"), e, M()).then(function (e) {
                                return e.data
                            }).then(function (e) {
                                if (!e.success) throw new Error;
                                return e.response
                            })
                        }({
                            publicKey: btoa(String.fromCharCode.apply(null, new Uint8Array(n))),
                            auth: btoa(String.fromCharCode.apply(null, new Uint8Array(a))),
                            notificationEndPoint: t
                        })
                    })) : ne(t, e)
                })
            }
        }()
    }
}, [[39, 1, 2]]]);
//# sourceMappingURL=main.2b16d139.chunk.js.map