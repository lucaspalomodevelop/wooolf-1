import React from "react";
import { Navigate, Route, Routes } from "react-router";

const NotFound = React.lazy(() => import("@pages/generic/NotFound"));
const StartPage = React.lazy(() => import("@pages/startpage/StartPage"));
const NamingPage = React.lazy(() => import("@pages/startpage/NamingPage"));
const Lobby = React.lazy(() => import("@pages/lobby/Lobby"));

function AppRoutes() {
	// let navigate = useNavigate();

	return (
		<Routes>
			<Route path="/" element={<Navigate to="/namingpage" />} />
			<Route path="/startpage" element={<StartPage />} />
			<Route path="/namingpage" element={<NamingPage />} />
			<Route path="/lobby" element={<Lobby />} />
			<Route path="*" element={<NotFound />} />
		</Routes>
	);
}

export default AppRoutes;
