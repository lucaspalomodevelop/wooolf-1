import React from "react";
import { Navigate, Route, Routes } from "react-router";

const NotFound = React.lazy(() => import("@pages/generic/NotFound"));
const StartPage = React.lazy(() => import("@pages/startpage/StartPage"));

function AppRoutes() {
	// let navigate = useNavigate();

	return (
		<Routes>
            <Route path="/" element={<Navigate to="/startpage" />} />
			<Route path="/startpage" element={<StartPage/>} /> 
			<Route path="*" element={<NotFound />} />
		</Routes>
	);
}

export default AppRoutes;
