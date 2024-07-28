import {INavData} from '@coreui/angular';

export const navItems: INavData[] = [
    /*{
        name: 'Dashboard',
        url: '/dashboard',
        iconComponent: {name: 'cil-speedometer'},
        badge: {
            color: 'info',
            text: 'NEW'
        }
    },*/
    {
        name: '',
        url: '/medicines',
        icon: "fa-solid fa-pills",
        attributes: {
            key: 'navbar.medicines'
        }
    },
    {
        name: '',
        url: '/stock',
        icon: "fa-solid fa-box-archive",
        attributes: {
            key: 'navbar.stock'
        }
    },
    {
        name: '',
        url: '/medication',
        icon: "fa-solid fa-tablets",
        attributes: {
            key: 'navbar.medication'
        }
    },
    {
        name: '',
        url: '/settings',
        icon: "fa-solid fa-gear",
        attributes: {
            key: 'navbar.settings'
        }
    }

];
