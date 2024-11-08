import { Button, Dropdown, Link, Text, TextInput } from "../core_elements";
import { getElementsByXpath, getProperty, waitFor } from "../utils";

/**
 * Represents the search options on the FRC CODEx Filing Index page.
 */
export class Search {
    #codexPage;
    advancedSearch;

    constructor(codexPage) {
        this.#codexPage = codexPage;
        this.advancedSearch = new AdvancedSearch(this.#codexPage);
        this.advancedSearchToggle = new Button(this.#codexPage,
            '//span[contains(text(),"Advanced Search")]', 'Advanced Search');
        this.companyNameInput = new TextInput(this.#codexPage,
            '//*[@id="company-name"]', 'Company Name');
        this.companyNumberInput = new TextInput(this.#codexPage,
            '//*[@id="company-number"]', 'Company Number');
        this.registry = new Dropdown(this.#codexPage,'//*[@id="registryCode"]', 'Registry Code');
        this.submitButton = new Button(this.#codexPage,
            '//button[@type="submit"]', 'Submit');
    }

    /**
     * Asserts that the search results contain the expected number of results.
     * @param expectedCount
     * @returns {Promise<void>}
     */
    async assertResultCount(expectedCount) {
        this.#codexPage.log(`Asserting result count is ${expectedCount}`);
        await waitFor(async () => {
            const cards = await this._getResultCards()
            if (cards.length !== expectedCount) {
                throw new Error(`Expected ${expectedCount} results, but found ${cards.length}`);
            }
        });
    }

    /**
     * Returns the first search result card with the specified name, crn, doc date, and/or filing date.
     * all values are optional and will be ignored if not provided.
     * @param {string} name - The name of the company to search for.
     * @param {string} crn - The company registration number to search for.
     * @param {string} docDate - The document date to search for.
     * @param {string} filingDate - The filing date to search for.
     * @returns {Promise<SearchResultCard>} - The search result card that matches the specified criteria.
     */
    async getSearchResult(name = '', crn = '', docDate = '', filingDate = '') {
        let result = null;
        await waitFor(async () => {
            const cards = await this._getResultCards();
            for (const card of cards) {
                const [cardName, cardCrn,
                    cardDocDate, cardFilingDate] = await Promise.all([
                    await card.companyName.getText(),
                    await card.crn.getText(),
                    await card.documentDate.getText(),
                    await card.filingDate.getText()
                ]);

                if ((name === '' || cardName === name) &&
                    (crn === '' || cardCrn === crn) &&
                    (docDate === '' || cardDocDate === docDate) &&
                    (filingDate === '' || cardFilingDate === filingDate)) {
                    result = card;
                    break;
                }
            }
            if (result === null) {
                throw new Error(`No search result found with name: ${name}, doc date: ${docDate}, filing date: ${filingDate}`);
            }
        }, 30000, 500);
        return result;
    }

    /**
     * Gets the search result cards from the search results.
     * @returns {Promise<SearchResultCard[]>} - for each search result.
     * @private
     */
    async _getResultCards() {
        const elements = await getElementsByXpath(this.#codexPage.page, '//*[contains(@id,"result")]');
        if (elements.length === 0) {
            return elements;
        }
        return await Promise.all(elements.map(async (e) => {
            const elementId = await getProperty(e, 'id');
            return new SearchResultCard(this.#codexPage, `//*[@id="${elementId}"]`);
        }));
    }
}

/**
 * Represents the advanced search options on the FRC CODEx Filing Index page.
 */
export class AdvancedSearch {
    #codexPage;

    constructor(codexPage) {
        this.#codexPage = codexPage;
        this.minDocDateYear = new TextInput(this.#codexPage,
            '//*[@id="min-document-date-year"]', 'Min Document Date Year');
        this.minDocDateMonth = new TextInput(this.#codexPage,
            '//*[@id="min-document-date-month"]', 'Min Document Date Month');
        this.minDocDateDay = new TextInput(this.#codexPage,
            '//*[@id="min-document-date-day"]', 'Min Document Date Day');
        this.maxDocDateYear = new TextInput(this.#codexPage,
            '//*[@id="max-document-date-year"]', 'Max Document Date Year');
        this.maxDocDateMonth = new TextInput(this.#codexPage,
            '//*[@id="max-document-date-month"]', 'Max Document Date Month');
        this.maxDocDateDay = new TextInput(this.#codexPage,
            '//*[@id="max-document-date-day"]', 'Max Document Date Day');
        this.minFilingDateYear = new TextInput(this.#codexPage,
            '//*[@id="min-filing-date-year"]', 'Min Filing Date Year');
        this.minFilingDateMonth = new TextInput(this.#codexPage,
            '//*[@id="min-filing-date-month"]', 'Min Filing Date Month');
        this.minFilingDateDay = new TextInput(this.#codexPage,
            '//*[@id="min-filing-date-day"]', 'Min Filing Date Day');
        this.maxFilingDateYear = new TextInput(this.#codexPage,
            '//*[@id="max-filing-date-year"]', 'Max Filing Date Year');
        this.maxFilingDateMonth = new TextInput(this.#codexPage,
            '//*[@id="max-filing-date-month"]', 'Max Filing Date Month');
        this.maxFilingDateDay = new TextInput(this.#codexPage,
            '//*[@id="max-filing-date-day"]', 'Max Filing Date Day');
    }
}

/**
 * Represents a search result card on the FRC CODEx Filing Index page.
 */
export class SearchResultCard {
    #codexPage;
    companyName;
    crn;
    documentDate;
    filingButton;
    filingDate;
    #locator;
    viewerButton;

    constructor(codexPage, locator) {
        this.#codexPage = codexPage;
        this.#locator = locator;
        this.companyName = new Link(this.#codexPage,
            `${this.#locator}//h3`, 'Company Name');
        this.crn = new Link(this.#codexPage,
            `${this.#locator}//dt[span[text()='CRN']]/following-sibling::dd/a`, 'CRN');
        this.documentDate = new Text(this.#codexPage,
            `${this.#locator}//dt[contains(text(), 'Document Date')]/following-sibling::dd`,
            'Document Date');
        this.filingButton = new Button(this.#codexPage,
            `${this.#locator}//a[normalize-space(text())="Filing"]`,
            'Select Button');
        this.filingDate = new Text(this.#codexPage,
            `${this.#locator}//dt[contains(text(), 'Filing Date')]/following-sibling::dd`,
            'Filing Date');
        this.viewerButton = new Button(this.#codexPage,
            `${this.#locator}//a[normalize-space(text())="Viewer"]`, 'Viewer Button');
    }
}