package com.fob.manager;

import java.util.ArrayList;

import com.fob.page.base.FOBPageBase;

public class FOBPageManager {

	public static ArrayList<FOBPageBase> sAlivePages = new ArrayList<FOBPageBase>();

	public static void registerAlivePage(FOBPageBase page) {
		if (page != null) {
			sAlivePages.add(page);
		}
	}

	public static void unregisterAlivePage(FOBPageBase page) {
		sAlivePages.remove(page);
	}

	public static void destroyAllAlivePages() {
		ArrayList<FOBPageBase> tmpPages = new ArrayList<FOBPageBase>();
		tmpPages.addAll(sAlivePages);

		int cnt = tmpPages.size();
		for (int i = cnt - 1; i >= 0; i--) {
			FOBPageBase page = tmpPages.get(i);
			page.closePage();
		}
		tmpPages.clear();
		sAlivePages.clear();
	}

	public static void destroyAlivePagesExcept(FOBPageBase except) {
		ArrayList<FOBPageBase> tmpPages = new ArrayList<FOBPageBase>();
		tmpPages.addAll(sAlivePages);

		int cnt = tmpPages.size();
		for (int i = cnt - 1; i >= 0; i--) {
			FOBPageBase page = tmpPages.get(i);
			if (except != page) {
				try {
					page.closePage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		tmpPages.clear();
		sAlivePages.clear();
		sAlivePages.add(except);
	}

	public static void destroyAlivePagesAfter(FOBPageBase except) {
		ArrayList<FOBPageBase> tmpPages = new ArrayList<FOBPageBase>();
		tmpPages.addAll(sAlivePages);

		int cnt = tmpPages.size();
		for (int i = cnt - 1; i >= 0; i--) {
			FOBPageBase page = tmpPages.get(i);
			if (except != page) {
				page.closePage();
			} else {
				break;
			}
		}
		sAlivePages.clear();
		sAlivePages.addAll(tmpPages);
	}

	public static FOBPageBase getLastPage() {
		if (!sAlivePages.isEmpty()) {
			return sAlivePages.get(sAlivePages.size() - 1);
		}
		return null;
	}

	public static boolean lastPageIsTypeOf(Class<?> pageType) {
		FOBPageBase page = getLastPage();
		return page != null && pageType.isInstance(page);
	}
}
