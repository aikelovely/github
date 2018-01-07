<%@ page import="ru.alfabank.dmpr.infrastructure.spring.security.UserContext" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userName = UserContext.getUser().getDisplayName();
    boolean isAdmin = UserContext.isAdminRole();
    boolean isSqlViewer = UserContext.isSqlViewerRole();

    boolean isCardUser = UserContext.isCardUser();
    boolean isPilAndCCUser = UserContext.isPilAndCCUser();
    boolean isZPUser = UserContext.isZPUser();
%>

<nav class="navbar navbar-default navbar-static-top" style="min-width: 768px">
    <div class="container-fluid">
        <div class="navbar-header">
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href='<c:url value="/" />' class="navbar-brand">OpEx</a>
        </div>

        <div class="collapse navbar-collapse" id="navbar-main">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" role="button">
                        Корпоративный бизнес
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <% if (isAdmin || UserContext.isCIBUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/TTYAndTTM" />'>
                                TTY, TTM1, TTM2
                            </a>
                        </li>
                        <li>
                            <a href='<c:url value="/showcase/clientTime" />'>
                                Декомпозиция процессов
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>TTY, TTM1, TTM2</a>
                        </li>
                        <li class="disabled">
                            <a>Декомпозиция процессов</a>
                        </li>
                        <% } %>
                    </ul>
                </li>
                <% if (isAdmin || isCardUser || isPilAndCCUser || isZPUser) { %>
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" role="button">
                        Розничный бизнес
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <% if (isAdmin || isCardUser) { %>
                        <li>
                            <a href='<c:url value="/showcase/cardsDeliveryPeriod" />'>
                                Срок доставки пластиковых карт в отделения
                            </a>
                        </li>
                        <li>
                            <a href='<c:url value="/showcase/cardsCustomersPercent" />'>
                                Доля клиентов, получивших карту в указанном
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Срок доставки пластиковых карт в отделения</a>
                        </li>
                        <li class="disabled">
                            <a>Доля клиентов, получивших карту в указанном</a>
                        </li>
                        <% } %>

                        <li class="nav-divider"></li>

                        <% if (isAdmin || isPilAndCCUser) { %>
                        <li>
                            <a href='<c:url value="/showcase/pilAndCC" />'>
                                Персональные кредиты и кредитные карты. Витрина декомпозиции
                            </a>
                            <a href='<c:url value="/showcase/operKpiHunter" />'>
                                Отчет по выполнению KPI - Hunter
                            </a>
                            <a href='<c:url value="/showcase/operKpiRbp" />'>
                                Отчет по выполнению KPI - Урегулирование КП
                            </a>
                            <a href='<c:url value="/showcase/operKpiScor" />'>
                                Отчет по выполнению KPI - Скоринги
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Персональные кредиты и кредитные карты. Витрина декомпозиции</a>
                        </li>
                        <li class="disabled">
                            <a>Отчет по выполнению KPI - Hunter</a>
                        </li>
                        <li class="disabled">
                            <a>Отчет по выполнению KPI - Урегулирование КП</a>
                        </li>
                        <li class="disabled">
                            <a>Отчет по выполнению KPI - Скоринги</a>
                        </li>
                        <% } %>

                        <li class="nav-divider"></li>
                        <% if (isAdmin || UserContext.isZPUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/zpOpeningSpeed" />'>
                                Скорость заведения зарплатного проекта
                            </a>
                        </li>
                        <li>
                            <a href='<c:url value="/showcase/zpInstitution" />'>
                                Заведение зарплатного проекта
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Скорость заведения зарплатного проекта</a>
                        </li>
                        <li class="disabled">
                            <a>Заведение зарплатного проекта</a>
                        </li>
                        <% } %>
                    </ul>
                </li>
                <% } %>
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" role="button">
                        Открытие клиентских счетов
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <% if (isAdmin || UserContext.isMassUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/massOpenAccount" />'>
                                Открытие клиентских счетов. KPI и метрики
                            </a>
                        </li>
                        <li>
                            <a href='<c:url value="/showcase/massDecomposition" />'>
                                Открытие клиентских счетов. Декомпозиция
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled"><a>Открытие клиентских счетов. KPI и метрики</a></li>
                        <li class="disabled"><a>Открытие клиентских счетов. Декомпозиция</a></li>
                        <% } %>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" role="button">
                        Операционный блок
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <% if (isAdmin || UserContext.isNomUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/nom" />'>
                                Количество конечных продуктов
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Количество конечных продуктов для расчета UC ОБ</a>
                        </li>
                        <% } %>

                        <% if (isAdmin || UserContext.isCapacityUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/workload" />'>
                                Нагрузка
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Нагрузка</a>
                        </li>
                        <% } %>

                        <li class="nav-divider"></li>

                        <% if (isAdmin || UserContext.isKpiObUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/leaderBoard" />'>
                                КПЭ ОБ
                            </a>
                        </li>
                        <li>
                            <a href='<c:url value="/showcase/leaderBoard2" />'>
                                LeaderBoard
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>КПЭ ОБ</a>
                        </li>
                        <% } %>

                        <% if (isAdmin || UserContext.isUCUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/unitCost" />'>
                                UnitCost
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>UnitCost</a>
                        </li>
                        <% } %>

                        <li class="nav-divider"></li>

                        <% if (isAdmin || UserContext.isCTQUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/obQuality" />'>
                                Показатели качества ОБ
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Показатели качества ОБ</a>
                        </li>
                        <% } %>

                        <% if (isAdmin || UserContext.isCTQBankUser()) { %>
                        <li>
                            <a href='<c:url value="/showcase/CTQDashboard" />'>
                                Витрина показателей CTQ
                            </a>
                        </li>
                        <li class="nav-divider"></li>
                        <li>
                            <a href='<c:url value="/showcase/qualityIndicatorsDss" />'>
                                Оформление договорных документов кредита/гарантии. Декомпозиция
                            </a>
                        </li>
                        <li>
                            <a href='<c:url value="/showcase/qualityIndicatorsDss2" />'>
                                Оформление договорных документов факторинга. Декомпозиция
                            </a>
                        </li>
                        <% } else { %>
                        <li class="disabled">
                            <a>Витрина показателей CTQ</a>
                        </li>
                        <li class="disabled">
                            <a>Показатели качества ДСС с детализацией до сотрудников</a>
                        </li>
                        <% } %>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <% if (isSqlViewer || isAdmin) { %>
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" role="button">
                        <i class="fa fa-user"></i>
                        <%= userName %>
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <% if (isAdmin) { %>
                        <li>
                            <a data-bind="visible: $root, click: clearDatabaseCache" href="javascript:;"
                               style="display: none">
                                <i class="fa fa-refresh"></i>
                                Сбросить кэш
                            </a>
                        </li>
                        <% } %>
                        <% if (isSqlViewer) { %>
                        <li>
                            <a data-bind="click: toggleSQLVisibility" href="javascript:;">
                                <i data-bind="visible: showSQL" class="fa fa-check"></i>
                                Показывать SQL
                            </a>
                        </li>
                        <% } %>
                    </ul>
                </li>
                <% } else { %>
                <li class="username"><i class="fa fa-user"></i> <%= userName %>
                </li>
                <% } %>
            </ul>
            <c:if test="${supportBtnMarkup != null && supportBtnMarkup.length() > 0}">
                <form class="navbar-form navbar-right">
                    <a class="btn btn-default btn-xs navbar-support-button">Нужна помощь?</a>
                </form>
                <script type="text/html" id="supportBtnMarkup">
                        <c:out value="${supportBtnMarkup}" escapeXml="false"></c:out>
                </script>
            </c:if>
        </div>
    </div>
</nav>
